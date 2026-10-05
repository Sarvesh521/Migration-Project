package com.example.app.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class CreateDriveRequest {

    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String driveType;

    public String getDriveType() {
        return this.driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    private LocalDate startDate;

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    private LocalDate endDate;

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    private LocalDate editStartDate;

    public LocalDate getEditStartDate() {
        return this.editStartDate;
    }

    public void setEditStartDate(LocalDate editStartDate) {
        this.editStartDate = editStartDate;
    }

    private LocalDate editEndDate;

    public LocalDate getEditEndDate() {
        return this.editEndDate;
    }

    public void setEditEndDate(LocalDate editEndDate) {
        this.editEndDate = editEndDate;
    }

    private LocalDate jeeEditingEndDate;

    public LocalDate getJeeEditingEndDate() {
        return this.jeeEditingEndDate;
    }

    public void setJeeEditingEndDate(LocalDate jeeEditingEndDate) {
        this.jeeEditingEndDate = jeeEditingEndDate;
    }

    private String details;

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
