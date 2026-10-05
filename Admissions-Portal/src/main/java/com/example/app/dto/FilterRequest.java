package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class FilterRequest {

    private String driveId;

    public String getDriveId() {
        return this.driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    private String programme;

    public String getProgramme() {
        return this.programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
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
