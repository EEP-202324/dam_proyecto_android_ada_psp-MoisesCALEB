package com.universidad.UniTechOK.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.universidad.UniTechOK.User;
import com.universidad.UniTechOK.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    public User createUser(User user) {
        // Aquí podrías agregar lógica para validar o procesar el usuario antes de guardarlo en la base de datos
        return userRepository.save(user);
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
