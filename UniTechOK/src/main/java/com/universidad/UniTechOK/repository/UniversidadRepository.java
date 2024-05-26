package com.universidad.UniTechOK.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.universidad.UniTechOK.Universidad;

@Repository
public interface UniversidadRepository extends CrudRepository<Universidad, Long> {
	
	Page<Universidad> findAll(Pageable pageable);
    // Puedes agregar métodos personalizados de consulta aquí si es necesario
}
