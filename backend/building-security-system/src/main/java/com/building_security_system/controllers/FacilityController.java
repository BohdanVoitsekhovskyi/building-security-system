package com.building_security_system.controllers;

import com.building_security_system.models.Facility;
import com.building_security_system.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("facility/create")
    public ResponseEntity<Facility> addFacility(@RequestBody Facility facility) {
        facility.setId(System.currentTimeMillis());
        return new ResponseEntity<>(facilityService.saveFacility(facility), HttpStatus.CREATED);
    }

    @DeleteMapping("facility/delete/{id}")
    public ResponseEntity<String> deleteFacility(@PathVariable long id) {
        facilityService.deleteFacilityById(id);
        return ResponseEntity.ok("Facility deleted");
    }
    @PostMapping("facility/{facilityId}/floor/{floorNumber}/create")
    public ResponseEntity<Facility> createFloor(@RequestBody String fileContent,
                                        @PathVariable long facilityId, @PathVariable int floorNumber){
        return new ResponseEntity<>(facilityService.updateFacility(facilityId,floorNumber,fileContent), HttpStatus.CREATED);
    }


}