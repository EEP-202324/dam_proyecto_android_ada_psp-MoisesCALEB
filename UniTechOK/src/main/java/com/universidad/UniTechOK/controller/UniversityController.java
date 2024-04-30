package com.universidad.UniTechOK.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.universidad.UniTechOK.Universidad;
import com.universidad.UniTechOK.service.UniversidadService;

import java.util.List;

@RestController
@RequestMapping("/api/universidades")
public class UniversityController {
    @Autowired
    private UniversidadService universityService;

    @GetMapping
    public List<Universidad> getAllUniversities() {
        return universityService.getAllUniversities();
    }

    @PostMapping
    public Universidad createUniversity(@RequestBody Universidad university) {
        return universityService.createUniversity(university);
    }

    // Otros métodos del controlador...
}
