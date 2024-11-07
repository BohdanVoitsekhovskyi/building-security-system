package com.building_security_system.service;

import com.building_security_system.Models.Detectors.Detector;
import org.bson.types.ObjectId;

import java.util.List;

public interface DetectorService {
    List<Detector> getDetectors();
    Detector getDetectorById(ObjectId id);
    Detector saveDetector(Detector detector);
    void deleteDetectorById(ObjectId id);
}