package com.example.demo.Controller;

import com.example.demo.Service.SeanceService;

import com.example.demo.gestionenicar.PresenceEnseignant;
import com.example.demo.gestionenicar.Seance;
import com.example.demo.repository.EnseignantRepository;
import com.example.demo.repository.PresenceEnseignantRepository;
import com.example.demo.repository.SeanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Collections;
import java.util.Date;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"}, 
allowedHeaders = "*",
allowCredentials = "true")
@RestController
@RequestMapping("/api/seances") 
public class SeanceController {
	@Autowired
    private SeanceService seanceService;
    
    @Autowired
    private SeanceRepository seanceRepository;
    
    @Autowired
    private PresenceEnseignantRepository presenceEnseignantRepository;
    
    @Autowired
    private EnseignantRepository enseignantRepository;

    @GetMapping
    public List<Seance> getAllSeances() {
        return seanceService.getAllSeances();
    }
    @GetMapping("/enseignant/{idEnseignant}")
    public List<Seance> getSeancesByEnseignant(
        @PathVariable("idEnseignant") Long idEnseignant) {  // Ajoutez le nom explicitement
        return seanceService.getSeancesByEnseignant(idEnseignant);
    }
    @GetMapping("/{idSeance}")
    public ResponseEntity<Seance> getSeanceById(@PathVariable Long idSeance) {
        return seanceRepository.findById(idSeance)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    

    @GetMapping("/date/{date}")
    public ResponseEntity<?> getSeancesByDate(
        @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        
        System.out.println("Date re�ue: " + date);  // Log 1
        
        try {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            System.out.println("Date convertie en sql.Date: " + sqlDate);  // Log 2
            
            List<Seance> seances = seanceRepository.findByDateseance(sqlDate);
            System.out.println("Nombre de s�ances trouv�es: " + seances.size());  // Log 3
            
            if (seances.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());  // Renvoie un tableau JSON vide plut�t que noContent()
            }
            
            // Pour le d�bogage, affichez les d�tails des s�ances
            seances.forEach(s -> System.out.println(
                "S�ance ID: " + s.getIdseance() + 
                ", Date: " + s.getDateseance() +
                ", Enseignant: " + (s.getEnseignant() != null ? s.getEnseignant().getIdenseignant() : "null")
            ));
            
            return ResponseEntity.ok(seances);
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            return ResponseEntity.status(500).body(
                Map.of("error", e.getMessage(), "dateReceived", date.toString())
            );
        }
    }
    @GetMapping("/update-now")
    public ResponseEntity<String> triggerUpdateNow() {
        try {
            seanceService.updateSeanceDatesWeekly();
            return ResponseEntity.ok("Mise � jour des dates d�clench�e avec succ�s � " + new Date());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body("Erreur: " + e.getMessage());
        }
    }
}

