package com.patientservice.dto;


import java.util.Date;

public class PatientDTO {
    private Long userId;
    private String name;
    private String address;
    private String personalNumber;
    private Date dateOfBirth;

    public PatientDTO(Long userId, String name, String address, String personalNumber, Date dateOfBirth) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.personalNumber = personalNumber;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
