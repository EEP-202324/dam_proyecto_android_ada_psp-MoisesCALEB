package com.example.aula;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/universidades")
public class UniversidadController {

    @Autowired
    private UniversidadRepository universidadRepository;

    @GetMapping
    public List<Universidad> obtenerTodasLasUniversidades() {
        return universidadRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Universidad> obtenerUniversidadPorId(@PathVariable(value = "id") Long universidadId) {
        Universidad universidad = universidadRepository.findById(universidadId)
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada con el ID: " + universidadId));
        return ResponseEntity.ok().body(universidad);
    }

    // otros métodos para manipular universidades
}

