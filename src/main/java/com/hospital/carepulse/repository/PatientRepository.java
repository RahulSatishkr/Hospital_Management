package com.hospital.carepulse.repository;

import com.hospital.carepulse.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // JpaRepository provides all standard CRUD operations out of the box:
    // findById, findAll, save, deleteById, existsById, etc.
}
