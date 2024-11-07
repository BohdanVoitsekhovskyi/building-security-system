package com.building_security_system.service;

import com.building_security_system.Models.Facility;
import org.bson.types.ObjectId;

import java.util.List;

public interface FacilityService {
    List<Facility> getFacilities();
    Facility getFacilityById(ObjectId id);
    Facility saveFacility(Facility facility);
    void deleteFacilityById(ObjectId id);
}