package com.universidad.UniTechOK.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.universidad.UniTechOK.Universidad;

@Repository
public interface UniversidadRepository extends CrudRepository<Universidad, Long> {
    // Puedes agregar métodos personalizados de consulta aquí si es necesario
}
