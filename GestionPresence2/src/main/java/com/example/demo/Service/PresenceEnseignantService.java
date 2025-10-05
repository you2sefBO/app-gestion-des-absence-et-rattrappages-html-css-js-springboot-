package com.example.demo.Service;
import com.example.demo.gestionenicar.Notification;
import com.example.demo.gestionenicar.NotificationEns;
import com.example.demo.gestionenicar.Rattrapage;
import com.example.demo.gestionenicar.PresenceEnseignant;
import com.example.demo.gestionenicar.Seance;
import com.example.demo.repository.EnseignantRepository;
import com.example.demo.repository.NotificationEnsRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.PresenceEnseignantRepository;
import com.example.demo.repository.RattrapageRepository;
import com.example.demo.repository.SeanceRepository;

import lombok.extern.slf4j.Slf4j;

import com.example.demo.gestionenicar.Enseignant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date; // au lieu de java.sql.Date
import java.util.List;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.text.SimpleDateFormat;
import java.util.Locale;
@Service
@Slf4j 
public class PresenceEnseignantService {
    @Autowired
    private PresenceEnseignantRepository presenceenseignantRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private NotificationEnsRepository notificationEnsRepository;
    @Autowired
    private RattrapageRepository rattrapageRepository;
    public List<PresenceEnseignant> getAllPresencesEnseignants() {
        return presenceenseignantRepository.findAll();
    }
    /**
     * Gestion des présences enseignants
     * Fonctionnalités :
     * - Vérifie si l'appel est fait dans les 15 premières minutes
     * - Enregistre automatiquement l'absence si délai dépassé
     * - Met à jour le statut de présence
     * - on l'envoi une notification pour lui dire qu'il est absent et s'il veut faire un rattrapage
     *     */
    @Transactional
    public boolean verifierPresenceEnseignant(Long idSeance, Long idEnseignant) {
        Seance seance = seanceRepository.findById(idSeance)
            .orElseThrow(() -> new RuntimeException("Séance introuvable"));
        Enseignant enseignant = enseignantRepository.findById(idEnseignant)
            .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));

        LocalTime heureDebut = convertToLocalTime(seance.getHeureDebutseance());
        LocalTime heureFin = convertToLocalTime(seance.getHeureFinseance());
        
        // Nouvelle méthode de conversion sécurisée
        LocalDate dateSeance = convertToLocalDate(seance.getDateseance());
        
        LocalDateTime debutSeance = LocalDateTime.of(dateSeance, heureDebut);
        LocalDateTime finSeance = LocalDateTime.of(dateSeance, heureFin);

        boolean seanceTerminee = LocalDateTime.now().isAfter(finSeance);
        boolean appelFaitPendantSeance = presenceenseignantRepository
            .existsBySeanceAndEnseignantAndPresenceenseignantTrue(seance, enseignant);

        if (!seanceTerminee) {
            if (!appelFaitPendantSeance) {
                PresenceEnseignant presence = new PresenceEnseignant();
                presence.setEnseignant(enseignant);
                presence.setSeance(seance);
                presence.setPresenceenseignant(true);
                presenceenseignantRepository.save(presence);
            }
            return true;
        }

        if (!appelFaitPendantSeance) {
            PresenceEnseignant presence = new PresenceEnseignant();
            presence.setEnseignant(enseignant);
            presence.setSeance(seance);
            presence.setPresenceenseignant(false);
            presenceenseignantRepository.save(presence);
            envoyerNotificationAbsence(seance, enseignant);
            return false;
        }

        return true;
    }

    // Ajoutez cette nouvelle méthode utilitaire dans la classe
    private LocalDate convertToLocalDate(Date date) {
        // Si c'est déjà un java.sql.Date
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        // Pour java.util.Date
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void envoyerNotificationAbsence(Seance seance, Enseignant enseignant) {
        try {
            NotificationEns notification = new NotificationEns();
            
            // Formatage du message avec tous les détails
            String message = String.format(
                "Absence enregistrée pour le cours %s du %s de %s à %s",
                (seance.getEnseignant() != null && seance.getEnseignant().getCours() != null 
                 ? seance.getEnseignant().getCours().getNomcours() 
                 : "cours inconnu"),
                new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.FRENCH).format(seance.getDateseance()),
                seance.getHeureDebutseance(),
                seance.getHeureFinseance()
            );
            
            notification.setMessageens(message);
            notification.setDateEnvoiens(new Date());
            notification.setEnseignant(enseignant);
            notification.setSeance(seance);
            notification.setStatutens(NotificationEns.Statutens.ENVOYEE);
            
            notificationEnsRepository.saveAndFlush(notification);

            Rattrapage rattrapage = new Rattrapage();
            rattrapage.setEnseignant(enseignant);
            rattrapage.setClasse(seance.getClasse());
            rattrapage.setEtatValidation("En attente");
            rattrapageRepository.save(rattrapage);
            
        } catch (Exception e) {
            log.error("Échec d'envoi de notification pour la séance {}", seance.getIdseance(), e);
            throw new RuntimeException("Échec d'envoi de notification", e);
        }
    }
 // Ajoutez cette nouvelle méthode dans la classe
    private LocalTime convertToLocalTime(String heureStr) {
        try {
            // Supprime les espaces et sépare heures/minutes
            String heureNettoyee = heureStr.replace(" ", "").replace("h", ":");
            String[] parties = heureNettoyee.split(":");
            
            int heures = Integer.parseInt(parties[0]);
            int minutes = parties.length > 1 ? Integer.parseInt(parties[1]) : 0;
            
            return LocalTime.of(heures, minutes);
        } catch (Exception e) {
            throw new IllegalArgumentException("Format d'heure invalide: " + heureStr + 
                ". Le format attendu est 'HHhMM' ou 'HH:MM' (ex: 08h30 ou 14:45)");
        }
    }
    @Scheduled(cron = "0 0 * * * *")  // Exécuter toutes les minutes
    @Transactional
    public void verifierSeancesTerminees() {
        LocalDateTime now = LocalDateTime.now();
        // Récupérer uniquement les séances terminées sans présence
        List<Seance> seances = seanceRepository.findSeancesTermineesSansPresence(now.toLocalDate());

        for (Seance seance : seances) {
            LocalDate dateSeance = convertToLocalDate(seance.getDateseance());
            LocalTime heureFin = convertToLocalTime(seance.getHeureFinseance());
            LocalDateTime finSeance = LocalDateTime.of(dateSeance, heureFin);

            // Vérifier si la séance est terminée
            if (now.isAfter(finSeance)) {
                Enseignant enseignant = seance.getEnseignant();
                // Enregistrer l'absence
                PresenceEnseignant presence = new PresenceEnseignant();
                presence.setEnseignant(enseignant);
                presence.setSeance(seance);
                presence.setPresenceenseignant(false);
                presence.setAutomatique(true); // Marquer comme absence automatique
                // Convertir LocalDateTime en Date
                presence.setDateEnregistrement(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
                presenceenseignantRepository.save(presence);

                // Envoyer la notification
                envoyerNotificationAbsence(seance, enseignant);
            }
        }
    }
}