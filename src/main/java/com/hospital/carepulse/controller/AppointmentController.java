package com.hospital.carepulse.controller;

import com.hospital.carepulse.model.Appointment;
import com.hospital.carepulse.model.Doctor;
import com.hospital.carepulse.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController // Combines @Controller + @ResponseBody: handles HTTP and auto-serialises to JSON
@RequestMapping // No base path — each method defines its own full path
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Constructor injection
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // =========================================================
    // GET /doctors
    // Lists all doctors. Optionally filter by specialty via query param.
    // Example: GET /doctors?specialty=Cardiology
    // =========================================================
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getDoctors(
            @RequestParam(required = false) String specialty) {

        List<Doctor> doctors = appointmentService.getDoctors(specialty);
        return ResponseEntity.ok(doctors);
    }

    // =========================================================
    // POST /appointments
    // Books a new appointment.
    // Request body (JSON):
    // {
    //   "doctorId": 1,
    //   "patientId": 2,
    //   "appointmentTime": "2025-09-15T10:00:00",
    //   "requiredSpecialty": "Cardiology"
    // }
    // Returns 201 Created on success.
    // Returns 400 Bad Request if specialty mismatches or slot is taken.
    // =========================================================
    @PostMapping("/appointments")
    public ResponseEntity<?> bookAppointment(@RequestBody Map<String, Object> body) {
        try {
            Long doctorId          = Long.valueOf(body.get("doctorId").toString());
            Long patientId         = Long.valueOf(body.get("patientId").toString());
            LocalDateTime time     = LocalDateTime.parse(body.get("appointmentTime").toString());
            String requiredSpecialty = body.get("requiredSpecialty").toString();

            Appointment booked = appointmentService.bookAppointment(
                    doctorId, patientId, time, requiredSpecialty);

            return ResponseEntity.status(HttpStatus.CREATED).body(booked);

        } catch (IllegalArgumentException | IllegalStateException ex) {
            // Business rule violation — return a clear 400 with the reason
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // =========================================================
    // PUT /appointments/{id}
    // Reschedules an existing appointment to a new time slot.
    // Re-verifies that the doctor is not already booked at the new time.
    // Request body (JSON):
    // {
    //   "newAppointmentTime": "2025-09-16T14:00:00"
    // }
    // Returns 200 OK with the updated appointment on success.
    // Returns 400 Bad Request if the new slot is already taken.
    // Returns 404 Not Found if the appointment ID doesn't exist.
    // =========================================================
    @PutMapping("/appointments/{id}")
    public ResponseEntity<?> rescheduleAppointment(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        try {
            LocalDateTime newTime = LocalDateTime.parse(body.get("newAppointmentTime").toString());

            Appointment updated = appointmentService.rescheduleAppointment(id, newTime);
            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException ex) {
            // Appointment not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        } catch (IllegalStateException ex) {
            // New time slot is already booked
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // =========================================================
    // DELETE /appointments/{id}
    // Cancels the appointment and frees the doctor's time slot.
    // Returns 200 OK with a confirmation message on success.
    // Returns 404 Not Found if the appointment ID doesn't exist.
    // =========================================================
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id) {
        try {
            appointmentService.cancelAppointment(id);
            return ResponseEntity.ok(
                    Map.of("message", "Appointment " + id + " has been successfully cancelled. The time slot is now free."));

        } catch (IllegalArgumentException ex) {
            // Appointment not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
