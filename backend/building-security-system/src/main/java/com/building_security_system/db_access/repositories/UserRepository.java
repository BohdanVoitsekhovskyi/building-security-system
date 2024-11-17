package com.building_security_system.db_access.repositories;

import com.building_security_system.db_access.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, Long> {
    UserEntity findOneByUsernameAndPassword(String username, String password);
    UserEntity findOneByUsername(String username);
}