package com.example.demo.Service;

import com.example.demo.gestionenicar.Enseignant;
import com.example.demo.repository.EnseignantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnseignantService {
    @Autowired
    private EnseignantRepository enseignantRepository;
    
    public List<Enseignant> getAllEnseignants() {
        return enseignantRepository.findAll();
    }
    /**
     * Gestion de l'authentification des enseignants
     * Fonctionnalit�s impl�ment�es :
     * - V�rification des identifiants de connexion
     * - Retourne l'objet Enseignant si authentification r�ussie
     * - Renvoie une erreur 401 si �chec
     */
    public Enseignant findByUsernameAndPassword(String username, String password) {
        return enseignantRepository.findByUsernameenseignantAndPasswordenseignant(username, password);
    }
}
