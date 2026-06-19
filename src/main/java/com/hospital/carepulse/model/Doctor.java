package com.hospital.carepulse.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Tells Hibernate to treat this class as a database table
@Table(name = "doctors") // Names the specific database table "doctors"
public class Doctor {

    @Id // Marks this field as the Primary Key (unique ID) for the table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically auto-increments the ID (1, 2, 3...)
    private Long id;

    private String name;      // Stores the doctor's full name
    private String specialty; // Stores the doctor's area of expertise (e.g., Cardiology)

    // Default constructor (Required by Spring Boot/JPA to load data)
    public Doctor() {}

    // Convenience constructor to easily create a new Doctor object with data
    public Doctor(String name, String specialty) {
        this.name = name;
        this.specialty = specialty;
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}