package com.example.demo.Controller;

import com.example.demo.Service.EmploiDuTempsService;
import com.example.demo.gestionenicar.EmploiDuTemps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emploi-du-temps")
public class EmploiDuTempsController {
    @Autowired
    private EmploiDuTempsService emploiDuTempsService;
    
    @GetMapping
    public List<EmploiDuTemps> getAllEmploiDuTemps() {
        return emploiDuTempsService.getAllEmploiDuTemps();
    }
}
