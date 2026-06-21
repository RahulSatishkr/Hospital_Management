package com.hospital.carepulse;

import com.hospital.carepulse.model.Doctor;
import com.hospital.carepulse.model.Patient;
import com.hospital.carepulse.repository.DoctorRepository;
import com.hospital.carepulse.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DataSeeder: Runs once at startup to populate the DB with sample doctors and patients.
 * This makes it easy to test all API endpoints immediately without manual setup.
 *
 * Seeded Doctors:
 *   ID 1 — Dr. Alice Sharma   | Cardiology
 *   ID 2 — Dr. Bob Mehta      | Pediatrics
 *   ID 3 — Dr. Clara Roy      | Neurology
 *
 * Seeded Patients:
 *   ID 1 — Ravi Kumar    | ravi@example.com
 *   ID 2 — Priya Singh   | priya@example.com
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public DataSeeder(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public void run(String... args) {
        // Only seed if the table is empty (prevents duplicate inserts on restart)
        if (doctorRepository.count() == 0) {
            doctorRepository.save(new Doctor("Dr. Alice Sharma", "Cardiology"));
            doctorRepository.save(new Doctor("Dr. Bob Mehta",   "Pediatrics"));
            doctorRepository.save(new Doctor("Dr. Clara Roy",   "Neurology"));
            System.out.println("✅ [DataSeeder] 3 doctors seeded.");
        }

        if (patientRepository.count() == 0) {
            patientRepository.save(new Patient("Ravi Kumar",  "ravi@example.com"));
            patientRepository.save(new Patient("Priya Singh", "priya@example.com"));
            System.out.println("✅ [DataSeeder] 2 patients seeded.");
        }
    }
}
