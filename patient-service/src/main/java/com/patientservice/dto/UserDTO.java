package com.patientservice.dto;

import com.patientservice.model.Role;
import lombok.Getter;

public class UserDTO {
    // Getters and setters
    @Getter
    private Long userId;
    @Getter
    private String username;
    @Getter
    private String fullName;
    private Role roles;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRoles() {
        return roles;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }
}
