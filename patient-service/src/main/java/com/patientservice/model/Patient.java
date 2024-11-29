package com.patientservice.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Patient {



    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId; // Koppling till UserService genom userId

    private String name;

    private String address;
    private String personalNumber;
    private Date dateOfBirth;

    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
