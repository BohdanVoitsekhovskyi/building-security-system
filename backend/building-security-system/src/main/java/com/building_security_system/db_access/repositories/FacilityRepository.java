package com.building_security_system.db_access.repositories;

import com.building_security_system.db_access.entities.FacilityEntity;
import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FacilityRepository extends MongoRepository<FacilityEntity, String> {
    List<FacilityEntity> findAll();
    FacilityEntity findOneById(ObjectId id);
    FacilityEntity save(FacilityEntity facility);
    void deleteById(ObjectId id);
}
