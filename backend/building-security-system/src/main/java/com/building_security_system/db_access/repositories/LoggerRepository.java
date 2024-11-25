package com.building_security_system.db_access.repositories;


import com.building_security_system.db_access.entities.FacilityLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoggerRepository extends MongoRepository<FacilityLogEntity, Long> {
    FacilityLogEntity findOneById(long id);
}
