package com.hospital.carepulse.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Tells Hibernate to treat this class as a database table
@Table(name = "patients") // Names the specific database table "patients"
public class Patient {

    @Id // Marks this field as the Primary Key (unique ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically auto-increments the ID
    private Long id;

    private String name;  // Stores the patient's full name
    private String email; // Stores the patient's email address

    // Default constructor (Required by Spring Boot/JPA)
    public Patient() {}

    // Convenience constructor to create a Patient object quickly
    public Patient(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}