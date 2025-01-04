package com.patientservice.service;

import com.patientservice.dto.PatientDTO;
import com.patientservice.dto.UserDTO;
import com.patientservice.model.Patient;
import com.patientservice.repository.PatientRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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


    @Autowired
    private HttpServletRequest httpServletRequest; // För att hämta det inkommande tokenet

    private String getBearerToken() {
        String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("Missing Bearer Token in Header");
    }

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

        if (user == null || user.getId() == null) {
            System.out.println("UserDTO är null eller saknar userId");
            return null;
        }

        Optional<Patient> patientOpt = patientRepository.findByUserId(user.getId());
        if (patientOpt.isEmpty()) {
            System.out.println("Ingen patient hittades med userId: " + user.getUserId());
            return null;
        }

        PatientDTO patientDTO = toDTO(patientOpt.get(), user);
        return patientDTO;
    }

    // Convert Patient to DTO
    private PatientDTO toDTO(Patient patient) {
        UserDTO user = getUserById(patient.getUserId());
        return toDTO(patient, user);
    }

    private PatientDTO toDTO(Patient patient, UserDTO user) {
        System.out.println("Konverterar patient: " + patient + ", user: " + user);
        return new PatientDTO(
                patient.getUserId(),
                patient.getName(),
                patient.getPersonalNumber(),
                patient.getAddress(),
                patient.getDateOfBirth()
        );
    }


    // WebClient calls to UserService
    private UserDTO getUserById(Long userId) {
        try {
            String token = getBearerToken();
            UserDTO userDTO = webClientBuilder.build()
                    .get()
                    .uri(userServiceUrl + "/" + userId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token) // Skicka vidare token
                    .retrieve()
                    .bodyToMono(UserDTO.class)
                    .block();
            System.out.println("Token being sent: " + token);
            return userDTO;
        } catch (WebClientException e) {
            throw new RuntimeException("Error fetching user by ID: " + e.getMessage());
        }
    }

    private UserDTO getUserByUsername(String username) {
        try {
            String token = getBearerToken();
            return webClientBuilder.build()
                    .get()
                    .uri(userServiceUrl + "/by-username/" + username)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token) // Skicka vidare token
                    .retrieve()
                    .bodyToMono(UserDTO.class)
                    .block();
        } catch (WebClientException e) {
            throw new RuntimeException("Error fetching user by ID: " + e.getMessage());
        }
    }
}
