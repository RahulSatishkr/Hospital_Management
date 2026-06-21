package com.hospital.carepulse.repository;

import com.hospital.carepulse.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Finds all doctors whose specialty matches the given string (case-insensitive)
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}
