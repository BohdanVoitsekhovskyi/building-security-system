package com.building_security_system.service.impl;

import com.building_security_system.db_access.repositories.DetectorRepository;
import com.building_security_system.models.detectors.Detector;
import com.building_security_system.service.DetectorService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetectorServiceImpl implements DetectorService {
    private final DetectorRepository detectorRepository;

    @Autowired
    public DetectorServiceImpl(DetectorRepository detectorRepository) {
        this.detectorRepository = detectorRepository;
    }

    @Override
    public List<Detector> getDetectors() {
        return detectorRepository.findAll().stream().map(Detector::toModel).toList();
    }

    @Override
    public Detector getDetectorById(ObjectId id) {
        return Detector.toModel(detectorRepository.findOneById(id));
    }

    @Override
    public Detector saveDetector(Detector detector) {
        return Detector.toModel(detectorRepository.save(Detector.toEntity(detector)));
    }

    @Override
    public void deleteDetectorById(ObjectId id) {
        detectorRepository.deleteById(id);
    }
}