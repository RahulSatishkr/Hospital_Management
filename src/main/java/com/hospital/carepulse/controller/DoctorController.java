package com.hospital.carepulse.controller;

import com.hospital.carepulse.model.Doctor;
import com.hospital.carepulse.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<?> getDoctors(@RequestParam(value = "specialty", required = false) String specialty) {
        try {
            List<Doctor> doctors;
            if (specialty != null && !specialty.trim().isEmpty()) {
                doctors = doctorService.getDoctorsBySpecialty(specialty);
                if (doctors.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                            new ErrorResponse("No doctors found with specialty: " + specialty)
                    );
                }
            } else {
                doctors = doctorService.getAllDoctors();
            }
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse("Error fetching doctors: " + e.getMessage())
            );
        }
    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
