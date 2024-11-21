package com.building_security_system.service;

import com.building_security_system.models.Facility;
import com.building_security_system.models.detectors.Detector;

import java.util.List;

public interface FacilityService {
    Facility getFacilityById(long facilityId);
    Facility saveFacility(Facility facility);
    Facility updateFacility(long facilityId, int floorNo, String fileContent);
    Facility updateFloor(long facilityId, int floorNumber, List<Detector> detectors);
    Facility deleteFloor(long facilityId, int floorNumber);
    Facility deleteDetector(long facilityId, int floorNumber, long roomId, Detector.DetectorType detectorType);
}