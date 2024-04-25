package com.example.Unitech;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@Tag(name = "UserController", description="API de usuarios")
public class UserController {
    
    private final UserRepository userRepository;
    
    private UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping
    private ResponseEntity<List<Usuario>> findAll(Pageable pageable) {
        Page<Usuario> page = userRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(), // 0
                        pageable.getPageSize(), // 20
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "username"))
        ));
        return ResponseEntity.ok(page.getContent());
    }
    
    @GetMapping("/{userId}")
    @Operation(summary = "Obtener un usuario", description = "Obtener un usuario por su ID")
    private ResponseEntity<Usuario> findById(@PathVariable Long userId) {
        Optional<Usuario> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    private ResponseEntity<Void> createUser(@RequestBody Usuario newUser,
            UriComponentsBuilder ucb) {
        Usuario savedUser = userRepository.save(newUser);
        URI locationOfNewUser = ucb.path("/users/{id}").buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(locationOfNewUser).build();
    }
    @PutMapping("/{userId}")
    @Operation(summary = "Actualizar un usuario", description = "Actualizar un usuario existente por su ID")
    private ResponseEntity<Void> updateUser(@PathVariable Long userId, @RequestBody Usuario updatedUser) {
        Optional<Usuario> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Usuario existingUser = userOptional.get();
            // Actualizar los campos del usuario existente con los valores proporcionados en updatedUser
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setPassword(updatedUser.getPassword()); // Asegúrate de que el campo password esté encriptado si es necesario
            
            userRepository.save(existingUser);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{userId}")
    @Operation(summary = "Eliminar un usuario", description = "Eliminar un usuario existente por su ID")
    private ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        Optional<Usuario> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            userRepository.deleteById(userId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

