package com.universidad.UniTechOK.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Page<Universidad>> getAllUniversities(Pageable pageable) {
        Page<Universidad> universities = universityService.getAllUniversities(pageable);
        return ResponseEntity.ok(universities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Universidad> getUniversityById(@PathVariable Long id) {
        try {
            Universidad university = universityService.getUniversityById(id);
            if (university != null) {
                return ResponseEntity.ok(university);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Universidad> createUniversity(@RequestBody Universidad university) {
        Universidad createdUniversity = universityService.createUniversity(university);
        return ResponseEntity.status(201).body(createdUniversity); // 201 Created
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUniversity(@PathVariable Long id) {
        boolean deleted = universityService.deleteUniversity(id);
        if (deleted) {
            return ResponseEntity.ok("University with id " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("University with id " + id + " not found or unable to delete.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Universidad> updateUniversity(@PathVariable Long id, @RequestBody Universidad updatedUniversity) {
        Universidad updated = universityService.updateUniversity(id, updatedUniversity);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Otros métodos del controlador...
}
