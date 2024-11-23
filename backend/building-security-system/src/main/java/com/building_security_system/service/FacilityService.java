package com.building_security_system.service;

import com.building_security_system.models.Facility;
import com.building_security_system.models.Detector;

import java.util.List;

public interface FacilityService {
    Facility getFacilityById(long facilityId);
    Facility saveFacility(Facility facility);
    Facility updateFacility(long facilityId, int floorNo, String fileContent);
    Facility updateFloor(long facilityId, long floorId, List<Detector> detectors);
    void deleteFloor(long facilityId, long floorId);
}