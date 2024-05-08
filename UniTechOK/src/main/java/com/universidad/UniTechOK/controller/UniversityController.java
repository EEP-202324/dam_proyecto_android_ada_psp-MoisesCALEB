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
    
    @DeleteMapping("/{id}")
    public String deleteUniversity(@PathVariable Long id) {
        boolean deleted = universityService.deleteUniversity(id);
        if (deleted) {
            return "University with id " + id + " deleted successfully.";
        } else {
            return "University with id " + id + " not found or unable to delete.";
        }
    }

    // Otros métodos del controlador...
}
