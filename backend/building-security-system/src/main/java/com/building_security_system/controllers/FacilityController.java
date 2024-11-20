package com.building_security_system.controllers;

import com.building_security_system.dto.DetectorDto;
import com.building_security_system.models.Facility;
import com.building_security_system.models.Floor;
import com.building_security_system.models.detectors.Detector;
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
        return new ResponseEntity<>(facilityService.updateFacility(facilityId,floorNumber,fileContent), HttpStatus.CREATED);
    }

    @PutMapping("facility/{facilityId}/floor/{floorNo}/edit")
    public ResponseEntity<Facility> updateFloor(@RequestBody List<DetectorDto> detectors,
                                                @PathVariable long facilityId, @PathVariable int floorNo) {
        Facility facility = facilityService.getFacilityById(facilityId);

        Floor floor = facility
                .getFloors()
                .stream()
                .filter(f -> f.getFloorNumber() == floorNo)
                .toList()
                .getFirst();

        floor.setDetectors(detectors.stream().map(Detector::dtoToModel).toList());

        facility = facilityService.saveFacility(facility);

        return new ResponseEntity<>(facility, HttpStatus.OK);
    }
}