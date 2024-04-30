package com.universidad.UniTechOK.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.universidad.UniTechOK.Universidad;
import com.universidad.UniTechOK.repository.UniversidadRepository;

import java.util.List;

@Service
public class UniversidadService {
    @Autowired
    private UniversidadRepository universidadRepository;

    public List<Universidad> getAllUniversities() {
        return (List<Universidad>) universidadRepository.findAll();
    }

    public Universidad createUniversity(Universidad university) {
        // Aquí podrías agregar lógica para validar o procesar la universidad antes de guardarla en la base de datos
        return universidadRepository.save(university);
    }

    // Otros métodos del servicio...
}


