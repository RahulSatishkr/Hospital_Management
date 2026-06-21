package com.hospital.carepulse.repository;

import com.hospital.carepulse.model.Appointment;
import com.hospital.carepulse.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Custom query to find an existing appointment for a specific doctor at a specific time
    Optional<Appointment> findByDoctorAndAppointmentTime(Doctor doctor, LocalDateTime appointmentTime);
}