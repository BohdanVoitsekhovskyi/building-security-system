package com.building_security_system.db_access.repositories;

import com.building_security_system.db_access.entities.DetectorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DetectorRepository extends MongoRepository<DetectorEntity, String> {
    DetectorEntity findOneById(long id);
    void deleteById(long id);
}