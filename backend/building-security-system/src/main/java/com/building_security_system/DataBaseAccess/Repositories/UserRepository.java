package com.building_security_system.DataBaseAccess.Repositories;

import com.building_security_system.DataBaseAccess.Entities.UserEntity;
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
