package com.building_security_system.service.impl;

import com.building_security_system.db_access.entities.FacilityEntity;
import com.building_security_system.db_access.entities.FloorEntity;
import com.building_security_system.db_access.repositories.FacilityRepository;
import com.building_security_system.models.Facility;
import com.building_security_system.service.FacilityService;
import com.building_security_system.util.SvgToJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public Facility getFacilityById(long id) {
        return Facility.toModel(facilityRepository.findOneById(id));
    }

    @Override
    public Facility saveFacility(Facility facility) {
        return Facility.toModel(facilityRepository.save(Facility.toEntity(facility)));
    }

    @Override
    public void deleteFacilityById(long id) {
        facilityRepository.deleteById(id);
    }

    @Override
    public SvgToJsonParser.JsonContent updateFacility(long id, String fileContent) {
//        FacilityEntity entity = facilityRepository.findOneById(id);
//        entity.getFloors().add(new FloorEntity(id, SvgToJsonParser.parseToJson(fileContent), new ArrayList<>()));
//        return facilityRepository.updateById(id, entity);
        return null;
    }
}