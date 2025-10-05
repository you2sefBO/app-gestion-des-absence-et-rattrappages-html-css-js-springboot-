package com.example.demo.Controller;

import com.example.demo.Service.EnseignantService;
import com.example.demo.gestionenicar.Enseignant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
//Contrôleur pour gérer les opérations liées aux enseignants
@RestController
@RequestMapping("/api/enseignants")
public class EnseignantController {
    @Autowired
    private EnseignantService enseignantService;
 // Récupérer tous les enseignants
    @GetMapping
    public List<Enseignant> getAllEnseignants() {
        return enseignantService.getAllEnseignants();
    }
 // Authentifier un enseignant
    @PostMapping("/login")
    public ResponseEntity<?> loginEnseignant(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("usernameenseignant");
        String password = credentials.get("passwordenseignant");
        
        Enseignant enseignant = enseignantService.findByUsernameAndPassword(username, password);
        
        if (enseignant != null) {
            return ResponseEntity.ok(enseignant);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides");
        }
    }
}

