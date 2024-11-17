package com.building_security_system.service;

import com.building_security_system.models.Facility;

import java.util.List;

public interface FacilityService {
    List<Facility> getFacilities();
    Facility getFacilityById(long id);
    Facility saveFacility(Facility facility);
    void deleteFacilityById(long id);
}