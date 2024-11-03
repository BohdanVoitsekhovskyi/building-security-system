package com.building_security_system.DataBaseAccess.Repositories;

import com.building_security_system.DataBaseAccess.Entities.FacilityEntity;
import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FacilityRepository extends MongoRepository<FacilityEntity, String> {
    List<FacilityEntity> findAll();
    FacilityEntity findOneById(ObjectId id);
    FacilityEntity save(FacilityEntity facility);
    void deleteById(ObjectId id);
}
