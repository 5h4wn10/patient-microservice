package com.patientservice.service;

import com.patientservice.dto.PatientDTO;
import com.patientservice.dto.UserDTO;
import com.patientservice.model.Patient;
import com.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${user.service.url}")
    private String userServiceUrl;

    // Get all patients
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PatientDTO addPatient(PatientDTO patientDTO) {
        if (patientRepository.existsById(patientDTO.getUserId()))
            throw new IllegalArgumentException("Patient with this userId already exists.");

        Patient patient = new Patient();
        patient.setUserId(patientDTO.getUserId());
        patient.setName(patientDTO.getName());
        patient.setAddress(patientDTO.getAddress());
        patient.setPersonalNumber(patientDTO.getPersonalNumber());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());

        Patient savedPatient = patientRepository.save(patient);
        return toDTO(savedPatient);
    }

    // Update patient details
    public boolean updatePatient(Long id, PatientDTO updatedPatientDTO) {
        Optional<Patient> existingPatient = patientRepository.findById(id);

        if (existingPatient.isPresent()) {
            Patient patient = existingPatient.get();
            if (updatedPatientDTO.getAddress() != null) {
                patient.setAddress(updatedPatientDTO.getAddress());
            }
            if (updatedPatientDTO.getPersonalNumber() != null) {
                patient.setPersonalNumber(updatedPatientDTO.getPersonalNumber());
            }
            patientRepository.save(patient);
            return true;
        }
        return false;
    }

    // Get patient by ID
    public PatientDTO getPatientById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.map(this::toDTO).orElse(null);
    }

    // Get patient info for current user
    public PatientDTO getPatientForCurrentUser(String username) {
        UserDTO user = getUserByUsername(username);

        if (user == null || !user.getRoles().equals("PATIENT")) {
            return null;
        }

        return patientRepository.findByUserId(user.getUserId())
                .map(patient -> toDTO(patient, user))
                .orElse(null);
    }

    // Convert Patient to DTO
    private PatientDTO toDTO(Patient patient) {
        UserDTO user = getUserById(patient.getUserId());
        return toDTO(patient, user);
    }

    private PatientDTO toDTO(Patient patient, UserDTO user) {
        return new PatientDTO(
                patient.getUserId(),
                user.getFullName(),
                patient.getAddress(),
                patient.getPersonalNumber(),
                patient.getDateOfBirth()
        );
    }

    // WebClient calls to UserService
    private UserDTO getUserById(Long userId) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(userServiceUrl + "/" + userId)
                    .retrieve()
                    .bodyToMono(UserDTO.class)
                    .block();
        } catch (WebClientException e) {
            throw new RuntimeException("Error fetching user by ID: " + e.getMessage());
        }
    }

    private UserDTO getUserByUsername(String username) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(userServiceUrl + "username" + username)
                    .retrieve()
                    .bodyToMono(UserDTO.class)
                    .block();
        } catch (WebClientException e) {
            throw new RuntimeException("Error fetching user by ID: " + e.getMessage());
        }
    }
}
