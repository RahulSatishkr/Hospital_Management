package com.hospital.carepulse.repository;

import com.hospital.carepulse.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Conflict check: is the doctor already booked at this exact time?
    // Used during both booking (POST) and rescheduling (PUT) to enforce the no-double-booking rule.
    boolean existsByDoctorIdAndAppointmentTime(Long doctorId, LocalDateTime appointmentTime);
}
