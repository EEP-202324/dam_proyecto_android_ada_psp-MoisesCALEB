package com.universidad.UniTechOK.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.universidad.UniTechOK.User;
import com.universidad.UniTechOK.service.UserService;


@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping(consumes = {"application/json"})
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    
    @PostMapping("/{userId}/associateUniversity/{universityId}")
    public ResponseEntity<?> associateUserWithUniversity(@PathVariable Long userId, @PathVariable Long universityId) {
        userService.associateUserWithUniversity(userId, universityId);
        return ResponseEntity.ok().build();
    }

    
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
    
    

    // Otros métodos del controlador...
}

