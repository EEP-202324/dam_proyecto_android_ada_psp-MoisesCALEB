package com.universidad.UniTechOK.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.universidad.UniTechOK.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    // Puedes agregar métodos personalizados de consulta aquí si es necesario
}
