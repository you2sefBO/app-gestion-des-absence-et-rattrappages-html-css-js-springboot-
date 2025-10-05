package com.example.demo.Controller;

import com.example.demo.Service.AdminService;
import com.example.demo.gestionenicar.Admin;
import com.example.demo.gestionenicar.Enseignant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
//Contrôleur pour gérer les opérations liées aux administrateurs
@RestController
@RequestMapping("/api/admins")
public class AdminController {
    @Autowired
    private AdminService adminService;
 // Récupérer tous les administrateurs
    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }
    // Authentifier un admin
    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("usernameadmin");
        String password = credentials.get("passwordadmin");
        
        Admin admin = adminService.findByUsernameAndPassword(username, password);
        
        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides");
        }
    }
}
