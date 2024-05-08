package com.universidad.UniTechOK.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.universidad.UniTechOK.Universidad;
import com.universidad.UniTechOK.User;
import com.universidad.UniTechOK.repository.UniversidadRepository;
import com.universidad.UniTechOK.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UniversidadRepository universityRepository;

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public void associateUserWithUniversity(Long userId, Long universityId) {
    	
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));

        Universidad university = universityRepository.findById(universityId)
                                                     .orElseThrow(() -> new EntityNotFoundException("Universidad no encontrada con ID: " + universityId));

        user.setUniversidad(university);
        userRepository.save(user);
    }
    
    public User updateUser(Long id, User user) {
        // Verifica si el usuario existe
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            user.setId(id); // Asegura que el ID del usuario sea el mismo que se está actualizando
            return userRepository.save(user);
        }
        return null; // Usuario no encontrado
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Otros métodos del servicio...
}
