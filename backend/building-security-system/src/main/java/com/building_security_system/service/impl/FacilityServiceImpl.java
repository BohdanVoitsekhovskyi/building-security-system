package com.building_security_system.service.impl;

import com.building_security_system.db_access.repositories.FacilityRepository;
import com.building_security_system.models.Facility;
import com.building_security_system.models.Floor;
import com.building_security_system.models.Detector;
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
    public Facility getFacilityById(long facilityId) {
        return Facility.toModel(facilityRepository.findOneById(facilityId));
    }

    @Override
    public Facility saveFacility(Facility facility) {
        return Facility.toModel(facilityRepository.save(Facility.toEntity(facility)));
    }

    @Override
    public Facility updateFacility(long facilityId, int floorNo, String fileContent) {
        SvgToJsonParser.JsonContent jsonContent = SvgToJsonParser.parseToJson(fileContent);
        Floor floor = new Floor(System.currentTimeMillis(),
                floorNo,
                jsonContent,
                new ArrayList<>());

        Facility facility = Facility.toModel(facilityRepository.findOneById(facilityId));
        if (facility.getFloors() == null) {
            facility.setFloors(new ArrayList<>());
        }

        facility.getFloors().add(floor);
        return Facility.toModel(facilityRepository.save(Facility.toEntity(facility)));
    }

    @Override
    public Facility updateFloor(long facilityId, long floorId, List<Detector> detectors) {
        Facility facility = Facility.toModel(facilityRepository.findOneById(facilityId));

        Floor floor = facility
                .getFloors()
                .stream()
                .filter(f -> f.getId() == floorId)
                .toList()
                .getFirst();

        long id = System.currentTimeMillis();
        for (Detector detector : detectors) {
            detector.setId(id++);
        }

        floor.setDetectors(detectors);
        return Facility.toModel(facilityRepository.save(Facility.toEntity(facility)));
    }


    @Override
    public void deleteFloor(long facilityId, long floorId) {
        Facility facility = Facility.toModel(facilityRepository.findOneById(facilityId));

        Floor floor = facility
                .getFloors()
                .stream()
                .filter(f -> f.getId() == floorId)
                .toList()
                .getFirst();

        facility.getFloors().remove(floor);
        facilityRepository.save(Facility.toEntity(facility));
    }
}