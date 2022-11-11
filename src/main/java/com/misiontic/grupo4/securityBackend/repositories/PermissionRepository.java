package com.misiontic.grupo4.securityBackend.repositories;

import com.misiontic.grupo4.securityBackend.models.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Integer> {
}
