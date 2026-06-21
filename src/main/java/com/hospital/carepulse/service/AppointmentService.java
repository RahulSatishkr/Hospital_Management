package com.hospital.carepulse.service;

import com.hospital.carepulse.model.Appointment;
import com.hospital.carepulse.model.Doctor;
import com.hospital.carepulse.model.Patient;
import com.hospital.carepulse.repository.AppointmentRepository;
import com.hospital.carepulse.repository.DoctorRepository;
import com.hospital.carepulse.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service // Marks this as a Spring-managed service component (business logic layer)
public class AppointmentService {

    // --- Injected Repositories (Spring wires these in automatically) ---
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    // Constructor injection (preferred over @Autowired field injection)
    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    // =========================================================
    // GET /doctors  →  List all doctors, optionally by specialty
    // =========================================================
    public List<Doctor> getDoctors(String specialty) {
        if (specialty != null && !specialty.isBlank()) {
            // Filter: return only doctors whose specialty matches the query param
            return doctorRepository.findBySpecialtyIgnoreCase(specialty);
        }
        // No filter: return every doctor in the database
        return doctorRepository.findAll();
    }

    // =========================================================
    // POST /appointments  →  Book a new appointment
    // Business rules enforced here:
    //   1. Doctor must exist
    //   2. Patient must exist
    //   3. Doctor's specialty must match the requested specialty
    //   4. Doctor must not already be booked at the requested time
    // =========================================================
    public Appointment bookAppointment(Long doctorId, Long patientId,
                                       LocalDateTime appointmentTime,
                                       String requiredSpecialty) {

        // --- Rule 1: Doctor must exist ---
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Doctor not found with ID: " + doctorId));

        // --- Rule 2: Patient must exist ---
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Patient not found with ID: " + patientId));

        // --- Rule 3: Specialty must match ---
        // Compare ignoring case (e.g., "cardiology" == "Cardiology")
        if (!doctor.getSpecialty().equalsIgnoreCase(requiredSpecialty)) {
            throw new IllegalArgumentException(
                    "Specialty mismatch: Doctor specialises in '"
                    + doctor.getSpecialty()
                    + "' but the request requires '"
                    + requiredSpecialty + "'.");
        }

        // --- Rule 4: No double-booking at the same time slot ---
        if (appointmentRepository.existsByDoctorIdAndAppointmentTime(doctorId, appointmentTime)) {
            throw new IllegalStateException(
                    "Dr. " + doctor.getName() + " is already booked at " + appointmentTime
                    + ". Please choose a different time slot.");
        }

        // All checks passed — create and persist the appointment
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setRequiredSpecialty(requiredSpecialty);

        return appointmentRepository.save(appointment);
    }

    // =========================================================
    // PUT /appointments/{id}  →  Reschedule to a new time slot
    // Business rules enforced here:
    //   1. Appointment must exist
    //   2. New time slot must not already be booked by the same doctor
    //      (excluding the current appointment itself to avoid self-conflict)
    // =========================================================
    public Appointment rescheduleAppointment(Long appointmentId, LocalDateTime newTime) {

        // --- Rule 1: Appointment must exist ---
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Appointment not found with ID: " + appointmentId));

        Long doctorId = appointment.getDoctor().getId();

        // --- Rule 2: Check new time slot availability ---
        // We need to make sure the new time isn't taken by ANOTHER appointment.
        // existsByDoctorIdAndAppointmentTime checks all rows including this one,
        // so we only block if the conflict is at a *different* slot than the current one.
        if (!newTime.equals(appointment.getAppointmentTime())
                && appointmentRepository.existsByDoctorIdAndAppointmentTime(doctorId, newTime)) {
            throw new IllegalStateException(
                    "Dr. " + appointment.getDoctor().getName()
                    + " is already booked at " + newTime
                    + ". Please choose a different time slot.");
        }

        // Update the time and persist
        appointment.setAppointmentTime(newTime);
        return appointmentRepository.save(appointment);
    }

    // =========================================================
    // DELETE /appointments/{id}  →  Cancel an appointment
    // Frees the doctor's time slot so others can book it.
    // =========================================================
    public void cancelAppointment(Long appointmentId) {

        // Ensure the appointment actually exists before attempting deletion
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new IllegalArgumentException(
                    "Cannot cancel — Appointment not found with ID: " + appointmentId);
        }

        appointmentRepository.deleteById(appointmentId);
        // After this returns, the slot is free for other patients to book
    }
}
