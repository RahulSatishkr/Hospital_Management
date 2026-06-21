package com.hospital.carepulse.controller;

import com.hospital.carepulse.model.Appointment;
import com.hospital.carepulse.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointments")
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        try {
            // Passes incoming data to the service layer to validate and save the appointment
            Appointment savedAppointment = appointmentService.bookAppointment(appointment);
            // Returns HTTP 200 OK along with the newly saved appointment object
            return ResponseEntity.ok(savedAppointment);
        } catch (IllegalArgumentException e) {
            // If the doctor is double-booked or specialty is wrong, return the error message
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}