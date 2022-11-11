package com.misiontic.grupo4.securityBackend.repositories;

import com.misiontic.grupo4.securityBackend.models.Rol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends CrudRepository<Rol, Integer> {
}
