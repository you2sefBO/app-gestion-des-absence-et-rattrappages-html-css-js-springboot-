package com.example.demo.gestionenicar;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Entity
@Getter @Setter
public class EmploiDuTemps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jour;
    private String heureDebut;
    private String heureFin;
    private String salle;
    
    @ManyToOne
    @JoinColumn(name = "idcours")
    private Cours cours;
    
    @ManyToOne
    @JoinColumn(name = "idenseignant")
    private Enseignant enseignant;
    
    @ManyToOne
    @JoinColumn(name = "idclasse")
    private Classe classe;
}