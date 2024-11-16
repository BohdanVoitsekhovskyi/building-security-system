package com.building_security_system.service.impl;

import com.building_security_system.db_access.repositories.DetectorRepository;
import com.building_security_system.models.detectors.Detector;
import com.building_security_system.service.DetectorService;
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
    public Detector getDetectorById(long id) {
        return Detector.toModel(detectorRepository.findOneById(id));
    }

    @Override
    public Detector saveDetector(Detector detector) {
        return Detector.toModel(detectorRepository.save(Detector.toEntity(detector)));
    }

    @Override
    public List<Detector> saveDetectors(List<Detector> detectors) {
        return detectorRepository
                .saveAll(detectors
                        .stream()
                        .map(Detector::toEntity)
                        .toList()
                ).stream()
                .map(Detector::toModel)
                .toList();
    }

    @Override
    public void deleteDetectorById(long id) {
        detectorRepository.deleteById(id);
    }
}