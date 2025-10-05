package com.example.demo.gestionenicar;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
public class Seance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idseance;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)  // Sp�cifie que seule la date doit �tre stock�e
    private Date dateseance;
    
    private String heureDebutseance;
    private String heureFinseance;
    private String salleseance;
    
    @ManyToOne(fetch = FetchType.EAGER)  // Charge l'enseignant imm�diatement
    @JoinColumn(name = "idenseignant")
    private Enseignant enseignant;
    
    @ManyToOne(fetch = FetchType.EAGER)  // Charge la classe imm�diatement
    @JoinColumn(name = "idclasse")
    private Classe classe;
    public Seance(Long id) {
        this.idseance = id;
    }
    public Seance() {}
}
