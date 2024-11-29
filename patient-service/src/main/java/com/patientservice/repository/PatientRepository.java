package com.patientservice.repository;

import com.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUserId(Long userId); // Byt ut mot userId istället för User
}
