package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class JeeVerificationUpdateRequest {

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
