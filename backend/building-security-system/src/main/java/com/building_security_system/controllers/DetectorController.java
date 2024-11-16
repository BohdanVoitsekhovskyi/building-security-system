package com.building_security_system.controllers;

import com.building_security_system.models.detectors.Detector;
import com.building_security_system.service.DetectorService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@CrossOrigin
public class DetectorController {
    private final DetectorService detectorService;

    @Autowired
    public DetectorController(DetectorService detectorService) {
        this.detectorService = detectorService;
    }

    @GetMapping("detectors")
    public ResponseEntity<List<Detector>> getDetectors() {
        System.out.println(detectorService.getDetectors());
        return ResponseEntity.ok(detectorService.getDetectors());
    }

    @GetMapping("detector/{id}")
    public ResponseEntity<Detector> getDetector(@PathVariable("id") int id) {
        return ResponseEntity.ok(detectorService.getDetectorById(id));
    }

    @CrossOrigin
    @PostMapping("detector/create")
    public ResponseEntity<Detector> addDetector(@RequestBody Detector detector) {
        detector.setId(System.currentTimeMillis());
        System.out.println(detector);
        return new ResponseEntity<>(detectorService.saveDetector(detector), HttpStatus.CREATED);
    }

    @PostMapping("detector/create-many")
    public ResponseEntity<List<Detector>> addDetectors(@RequestBody List<Detector> detectors) {
        return new ResponseEntity<>(detectorService.saveDetectors(detectors), HttpStatus.CREATED);
    }

    @DeleteMapping("detector/{id}/delete")
    public ResponseEntity<String> deleteDetector(@PathVariable("id") int id) {
        detectorService.deleteDetectorById(id);
        return ResponseEntity.ok("Detector deleted");
    }
}