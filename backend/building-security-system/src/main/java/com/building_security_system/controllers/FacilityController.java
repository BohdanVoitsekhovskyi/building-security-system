package com.building_security_system.controllers;

import com.building_security_system.dto.DetectorDto;
import com.building_security_system.models.Facility;
import com.building_security_system.models.Detector;
import com.building_security_system.service.FacilityService;
import com.building_security_system.util.SvgToJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@CrossOrigin
public class FacilityController {
    private final FacilityService facilityService;

    @Autowired
    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;

    }

    @GetMapping("facility/{id}")
    public ResponseEntity<Facility> getFacility(@PathVariable("id") long id) {
        return ResponseEntity.ok(facilityService.getFacilityById(id));
    }

    @PutMapping("facility/{facilityId}/floor/{floorNumber}/create")
    public ResponseEntity<Facility> createFloor(@RequestBody String fileContent,
                                        @PathVariable long facilityId, @PathVariable int floorNumber){
        SvgToJsonParser.JsonContent.counter = 0;
        return new ResponseEntity<>(
                facilityService.updateFacility(facilityId, floorNumber, fileContent), HttpStatus.CREATED
        );
    }

    @PutMapping("facility/{facilityId}/floor/{floorNumber}/edit")
    public ResponseEntity<Facility> updateFloor(@RequestBody List<DetectorDto> detectors,
                                                @PathVariable long facilityId, @PathVariable int floorNumber) {
        Facility facility =
                facilityService.updateFloor(
                        facilityId, floorNumber, detectors.stream().map(Detector::dtoToModel).toList()
                );

        return new ResponseEntity<>(facility, HttpStatus.OK);
    }

    @DeleteMapping("facility/{facilityId}/floor/{floorNumber}/delete")
    public ResponseEntity<String> deleteFloor(@PathVariable long facilityId, @PathVariable int floorNumber) {
        facilityService.deleteFloor(facilityId, floorNumber);
        return new ResponseEntity<>("Floor successfully deleted", HttpStatus.OK);
    }
}