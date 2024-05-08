package com.universidad.UniTechOK.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.universidad.UniTechOK.Universidad;
import com.universidad.UniTechOK.User;
import com.universidad.UniTechOK.repository.UniversidadRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UniversidadService {
    @Autowired
    private UniversidadRepository universidadRepository;

    public List<Universidad> getAllUniversities() {
        return (List<Universidad>) universidadRepository.findAll();
    }

    public Universidad createUniversity(Universidad universidad) {
        // Aquí podrías agregar lógica para validar o procesar la universidad antes de guardarla en la base de datos
        // Antes de guardar la universidad, asigna la universidad a cada usuario en la lista de usuarios
        List<User> users = universidad.getUsuarios();
        if (users != null) {
            for (User user : users) {
                user.setUniversidad(universidad);
            }
        }
        return universidadRepository.save(universidad);
    }
    public boolean deleteUniversity(Long id) {
        Optional<Universidad> universityOptional = universidadRepository.findById(id);
        if (universityOptional.isPresent()) {
            universidadRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Otros métodos del servicio...
}


