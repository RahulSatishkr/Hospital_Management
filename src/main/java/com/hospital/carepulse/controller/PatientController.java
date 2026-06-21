package com.hospital.carepulse.controller;

import com.hospital.carepulse.model.Patient;
import com.hospital.carepulse.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<Patient> registerPatient(@RequestBody Patient patient) {
        Patient saved = patientService.registerPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}