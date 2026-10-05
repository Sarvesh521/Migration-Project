package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class StartApplicationRequest {

    private String driveId;

    public String getDriveId() {
        return this.driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }
}
