package com.example.Unitech;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/universidades")
public class UniversidadController {

    private final UniversidadRepository universidadRepository;

    public UniversidadController(UniversidadRepository universidadRepository) {
        this.universidadRepository = universidadRepository;
    }

    @GetMapping
    public ResponseEntity<List<Universidad>> getAllUniversidades() {
        List<Universidad> universidades = universidadRepository.findAll();
        return ResponseEntity.ok(universidades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Universidad> getUniversidadById(@PathVariable Long id) {
        Optional<Universidad> universidadOptional = universidadRepository.findById(id);
        return universidadOptional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createUniversidad(@RequestBody Universidad universidad) {
        Universidad savedUniversidad = universidadRepository.save(universidad);
        URI locationOfNewUniversidad = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUniversidad.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewUniversidad).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUniversidad(@PathVariable Long id, @RequestBody Universidad universidad) {
        if (!universidadRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        universidad.setId(id);
        universidadRepository.save(universidad);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversidad(@PathVariable Long id) {
        if (!universidadRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        universidadRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

