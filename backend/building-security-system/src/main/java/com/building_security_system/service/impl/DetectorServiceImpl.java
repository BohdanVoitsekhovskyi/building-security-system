//package com.building_security_system.service.impl;
//
//import com.building_security_system.db_access.repositories.DetectorRepository;
//import com.building_security_system.models.detectors.*;
//import com.building_security_system.service.DetectorService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class DetectorServiceImpl implements DetectorService {
//    private final DetectorRepository detectorRepository;
//
//    @Autowired
//    public DetectorServiceImpl(DetectorRepository detectorRepository) {
//        this.detectorRepository = detectorRepository;
//    }
//
//    @Override
//    public List<Detector> getDetectors() {
//        return detectorRepository.findAll().stream().map(Detector::toModel).toList();
//    }
//
//    @Override
//    public Detector getDetectorById(long id) {
//        return Detector.toModel(detectorRepository.findOneById(id));
//    }
//
//    @Override
//    public Detector saveDetector(Detector detector) {
//        if (detector.getDescription().equals(DoorDetector.class.getName())) {
//            detector = new DoorDetector(detector.getId());
//        } else if (detector.getDescription().equals(FloodDetector.class.getName())) {
//            detector = new FloodDetector(detector.getId());
//        } else if (detector.getDescription().equals(MotionDetector.class.getName())) {
//            detector = new MotionDetector(detector.getId());
//        } else if (detector.getDescription().equals(SmokeDetector.class.getName())) {
//            detector = new SmokeDetector(detector.getId());
//        } else if (detector.getDescription().equals(TemperatureDetector.class.getName())) {
//            detector = new TemperatureDetector(detector.getId());
//        } else if (detector.getDescription().equals(WindowDetector.class.getName())) {
//            detector = new WindowDetector(detector.getId());
//        }
//
//        return Detector.toModel(detectorRepository.save(Detector.toEntity(detector)));
//    }
//
//    @Override
//    public List<Detector> saveDetectors(List<Detector> detectors) {
//        return detectorRepository
//                .saveAll(detectors
//                        .stream()
//                        .map(Detector::toEntity)
//                        .toList()
//                ).stream()
//                .map(Detector::toModel)
//                .toList();
//    }
//
//    @Override
//    public void deleteDetectorById(long id) {
//        detectorRepository.deleteById(id);
//    }
//}