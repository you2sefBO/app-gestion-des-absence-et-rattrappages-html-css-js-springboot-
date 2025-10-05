package com.example.demo.Service;

import com.example.demo.gestionenicar.Admin;
import com.example.demo.gestionenicar.Etudiant;
import com.example.demo.repository.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    /**
     * Gestion de l'authentification des admins
     * Fonctionnalit�s impl�ment�es :
     * - V�rification des identifiants de connexion
     * - Retourne l'objet admin si authentification r�ussie
     * - Renvoie une erreur 401 si �chec
     */
    public Admin findByUsernameAndPassword(String username, String password) {
        return 	adminRepository.findByUsernameadminAndPasswordadmin(username, password);
    }
}
