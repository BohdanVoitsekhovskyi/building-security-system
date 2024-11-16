package com.building_security_system.db_access.repositories;

import com.building_security_system.db_access.entities.FacilityEntity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FacilityRepository extends MongoRepository<FacilityEntity, String> {
    FacilityEntity findOneById(long id);
    void deleteById(long id);
}