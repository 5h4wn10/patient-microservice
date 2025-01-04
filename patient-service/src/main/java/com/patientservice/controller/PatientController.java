package com.patientservice.controller;

import com.patientservice.dto.PatientDTO;
import com.patientservice.model.Patient;
import com.patientservice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;


    //@PreAuthorize("hasAnyRole('DOCTOR', 'STAFF')")
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        if (patientService.getAllPatients().isEmpty()) {
            return (ResponseEntity<List<PatientDTO>>) ResponseEntity.EMPTY;
        }
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO) {
        PatientDTO savedPatient = patientService.addPatient(patientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePatient(@PathVariable Long id, @RequestBody PatientDTO updatedPatientDTO) {
        boolean isUpdated = patientService.updatePatient(id, updatedPatientDTO);
        if (isUpdated) {
            return ResponseEntity.ok("Patient information has been updated.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
        }
    }

    //@PreAuthorize("hasAnyRole('PATIENT')")
    @GetMapping("/me")
    public ResponseEntity<?> getPatientInfo(Authentication authentication) {
        PatientDTO patient = patientService.getPatientForCurrentUser(authentication.getName());
        System.out.println(authentication.getName());
        System.out.println("FRÅN CONTROLLER: " + patient);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: User is not a patient.");
        }

        return ResponseEntity.ok(patient);
    }

    //@PreAuthorize("hasAnyAuthority('DOCTOR', 'STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        PatientDTO patient = patientService.getPatientById(id);

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(patient);
    }
}
