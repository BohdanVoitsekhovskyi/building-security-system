package com.building_security_system.service;

import com.building_security_system.models.detectors.Detector;

import java.util.List;

public interface DetectorService {
    List<Detector> getDetectors();
    Detector getDetectorById(long id);
    Detector saveDetector(Detector detector);
    List<Detector> saveDetectors(List<Detector> detectors);
    void deleteDetectorById(long id);
}