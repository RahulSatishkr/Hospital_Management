package com.hospital.carepulse.service;

import com.hospital.carepulse.model.Appointment;
import com.hospital.carepulse.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

// Marks this class as a Spring Service layer bean to handle core business logic
@Service
public class AppointmentService {
    // Dependency injection of the repository layer to perform database operations
    private final AppointmentRepository appointmentRepository;
    // Constructor to inject the AppointmentRepository instance
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment bookAppointment(Appointment appointment) {
        // 1. First Validation: Check specialty match (Harshith's logic)
        String doctorSpecialty = appointment.getDoctor().getSpecialty();
        String neededSpecialty = appointment.getRequiredSpecialty();

        // If the doctor has no specialty or it doesn't match the required specialty (ignoring case), block it
        if (doctorSpecialty == null || !doctorSpecialty.equalsIgnoreCase(neededSpecialty)) {
            throw new IllegalArgumentException("Selected doctor specialty does not match the required specialty!");
        }

        // 2. Second Validation: Prevent Double-Booking
        Optional<Appointment> existingAppointment = appointmentRepository.findByDoctorAndAppointmentTime(
                appointment.getDoctor(),
                appointment.getAppointmentTime()
        );

        if (existingAppointment.isPresent()) {
            // If an appointment is returned, this doctor is already busy! Block it.
            throw new IllegalArgumentException("This doctor is already booked for an appointment at this time!");
        }

        // If both checks pass, safely save the appointment
        return appointmentRepository.save(appointment);
    }
}