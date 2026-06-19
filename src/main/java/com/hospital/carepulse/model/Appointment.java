package com.hospital.carepulse.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity // Tells Hibernate to treat this class as a database table
@Table(name = "appointments") // Names the specific database table "appointments"
public class Appointment {

    @Id // Marks this field as the Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically auto-increments the ID
    private Long id;

    @ManyToOne // Relationship mapping: Many appointments can belong to One single Doctor
    @JoinColumn(name = "doctor_id") // Creates a Foreign Key column linking to the Doctor's ID
    private Doctor doctor;

    @ManyToOne // Relationship mapping: Many appointments can belong to One single Patient
    @JoinColumn(name = "patient_id") // Creates a Foreign Key column linking to the Patient's ID
    private Patient patient;

    private LocalDateTime appointmentTime; // Stores both the date and exact time of the booking
    private String requiredSpecialty;      // Stores the specific illness/specialty requested for validation

    // Default constructor (Required by Spring Boot/JPA)
    public Appointment() {}

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getRequiredSpecialty() { return requiredSpecialty; }
    public void setRequiredSpecialty(String requiredSpecialty) { this.requiredSpecialty = requiredSpecialty; }
}