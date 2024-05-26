package com.universidad.UniTechOK.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Universidad> getAllUniversities(Pageable pageable) {
        return universidadRepository.findAll(pageable);
    }
    
    public Universidad getUniversityById(Long id) {
        // Utiliza el método findById del repositorio para obtener la universidad por su ID
        Optional<Universidad> universityOptional = universidadRepository.findById(id);
        
        // Verifica si se encontró la universidad y devuelve el objeto Universidad si está presente
        return universityOptional.orElse(null);
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
    public Universidad updateUniversity(Long id, Universidad updatedUniversity) {
        // Verificar si la universidad con el ID dado existe en la base de datos
        Universidad existingUniversity = universidadRepository.findById(id)
            .orElseThrow();

        // Actualizar los campos de la universidad existente con los datos proporcionados
        existingUniversity.setNombre(updatedUniversity.getNombre());
        existingUniversity.setDireccion(updatedUniversity.getDireccion());
        existingUniversity.setEnlace(updatedUniversity.getEnlace());

        // Guardar la universidad actualizada en la base de datos
        return universidadRepository.save(existingUniversity);
    }



    // Otros métodos del servicio...
}


