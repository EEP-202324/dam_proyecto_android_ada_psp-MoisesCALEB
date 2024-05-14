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
    @GetMapping("/{id}")
    public Universidad getUniversityById(@PathVariable Long id) throws Exception {
        Universidad university = universityService.getUniversityById(id);
        if (university != null) {
            return university;
        } else {
            throw new Exception("University not found with id: " + id);
        }
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
    
    @PutMapping("/{id}")
    public Universidad updateUniversity(@PathVariable Long id, @RequestBody Universidad updatedUniversity) {
        return universityService.updateUniversity(id, updatedUniversity);
    }

    // Otros métodos del controlador...
}
