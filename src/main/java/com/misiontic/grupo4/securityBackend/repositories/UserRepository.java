package com.misiontic.grupo4.securityBackend.repositories;

import com.misiontic.grupo4.securityBackend.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}
