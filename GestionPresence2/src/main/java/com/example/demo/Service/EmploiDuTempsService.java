package com.example.demo.Service;

import com.example.demo.gestionenicar.EmploiDuTemps;
import com.example.demo.repository.EmploiDuTempsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmploiDuTempsService {
    @Autowired
    private EmploiDuTempsRepository emploidutempsRepository;
    
    public List<EmploiDuTemps> getAllEmploiDuTemps() {
        return emploidutempsRepository.findAll();
    }
}