package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class ApplicationSummary {

    private String id;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String applicationNumber;

    public String getApplicationNumber() {
        return this.applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    private String fullName;

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private String email;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String jeeMainVerificationStatus;

    public String getJeeMainVerificationStatus() {
        return this.jeeMainVerificationStatus;
    }

    public void setJeeMainVerificationStatus(String jeeMainVerificationStatus) {
        this.jeeMainVerificationStatus = jeeMainVerificationStatus;
    }

    private String jeeAdvancedVerificationStatus;

    public String getJeeAdvancedVerificationStatus() {
        return this.jeeAdvancedVerificationStatus;
    }

    public void setJeeAdvancedVerificationStatus(String jeeAdvancedVerificationStatus) {
        this.jeeAdvancedVerificationStatus = jeeAdvancedVerificationStatus;
    }
}
