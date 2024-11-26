package com.building_security_system.controllers;

import com.building_security_system.service.LoggerService;
import com.building_security_system.service.impl.LoggerServiceImpl;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CommonsLog
@RestController
@RequestMapping("/api")
@CrossOrigin
public class TestingController {
    LoggerService loggerService;

    @Autowired
    public TestingController(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    @GetMapping("/test/reactions/{id}")
    public ResponseEntity<?> getAllReactions(@PathVariable long id) {
        return ResponseEntity.ok(loggerService.getLog(id));

    }
    @GetMapping("/test/reactions/as-file/{id}")
    public ResponseEntity<?> getAllReactionsAsFile(@PathVariable long id) {

       byte[] fileContent = loggerService.getLogAsByteArray(id);
       HttpHeaders headers = new HttpHeaders();
       headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=log.tx");
       headers.add(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8");

       return new ResponseEntity<>(fileContent,headers, HttpStatus.OK);
    }
}