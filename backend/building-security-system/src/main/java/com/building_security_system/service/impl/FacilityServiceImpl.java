package com.building_security_system.service.impl;

import com.building_security_system.db_access.repositories.FacilityRepository;
import com.building_security_system.models.Facility;
import com.building_security_system.models.Floor;
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
    public Facility updateFacility(long id, int floorNumber, String fileContent) {
        SvgToJsonParser.JsonContent jsonContent = SvgToJsonParser.parseToJson(fileContent);
        Floor floor = new Floor(System.currentTimeMillis(),
                floorNumber,
                jsonContent,
                new ArrayList<>());

        Facility facility = Facility.toModel(facilityRepository.findOneById(id));
        if (facility.getFloors() == null) {
            facility.setFloors(new ArrayList<>());
        }

        facility.getFloors().add(floor);
        return Facility.toModel(facilityRepository.save(Facility.toEntity(facility)));
    }
}