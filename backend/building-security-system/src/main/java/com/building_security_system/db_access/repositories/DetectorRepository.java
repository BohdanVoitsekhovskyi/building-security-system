package com.building_security_system.db_access.repositories;

import com.building_security_system.db_access.entities.DetectorEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DetectorRepository extends MongoRepository<DetectorEntity, String> {
    List<DetectorEntity> findAll();
    DetectorEntity findOneById(ObjectId id);
    DetectorEntity save(DetectorEntity detector);
    void deleteById(ObjectId id);
}
