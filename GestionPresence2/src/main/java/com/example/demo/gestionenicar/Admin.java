package com.example.demo.gestionenicar;


import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idadmin;
    private String usernameadmin;
    private String passwordadmin;
}
