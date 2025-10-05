package com.example.demo.repository;

import com.example.demo.gestionenicar.NotificationEns;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationEnsRepository extends JpaRepository<NotificationEns, Long> {
    List<NotificationEns> findAllByOrderByDateEnvoiensDesc();
    
    // Changez pour utiliser le nom exact du champ
    List<NotificationEns> findByEnseignant_IdenseignantOrderByDateEnvoiensDesc(Long idenseignant);
}