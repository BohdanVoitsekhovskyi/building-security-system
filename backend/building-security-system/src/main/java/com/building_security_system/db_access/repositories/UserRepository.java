package com.building_security_system.db_access.repositories;

import com.building_security_system.db_access.entities.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface UserRepository extends MongoRepository<UserEntity, String> {
    List<UserEntity> findAll();
    UserEntity findOneByEmail(String email);
    UserEntity findOneByEmailAndPassword(String email, String password);
    UserEntity findOneById(ObjectId id);
    UserEntity save(UserEntity user);
}
