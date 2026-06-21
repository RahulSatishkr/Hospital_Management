package com.hospital.carepulse.service;

import com.hospital.carepulse.model.Patient;
import com.hospital.carepulse.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient registerPatient(Patient patient) {
        return patientRepository.save(patient);
    }
}