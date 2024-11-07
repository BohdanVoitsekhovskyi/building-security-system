package com.building_security_system.service.impl;

import com.building_security_system.db_access.Repositories.FacilityRepository;
import com.building_security_system.models.Facility;
import com.building_security_system.service.FacilityService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacilityServiceImpl implements FacilityService {
    private final FacilityRepository facilityRepository;

    @Autowired
    public FacilityServiceImpl(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    @Override
    public List<Facility> getFacilities() {
        return facilityRepository.findAll().stream().map(Facility::toModel).toList();
    }

    @Override
    public Facility getFacilityById(ObjectId id) {
        return Facility.toModel(facilityRepository.findOneById(id));
    }

    @Override
    public Facility saveFacility(Facility facility) {
        return Facility.toModel(facilityRepository.save(Facility.toEntity(facility)));
    }

    @Override
    public void deleteFacilityById(ObjectId id) {
        facilityRepository.deleteById(id);
    }
}