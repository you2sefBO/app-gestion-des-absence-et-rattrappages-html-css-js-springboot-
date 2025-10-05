package com.example.demo.repository;

import com.example.demo.gestionenicar.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByEtudiantIdetudiant(Long idetudiant);
}