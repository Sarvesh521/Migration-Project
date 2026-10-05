package com.example.app.service;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.*;
import com.example.app.entity.*;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.DriveRepository;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service()
public class DriveService {

    @MethodMetadata(irId = "get_drive_by_id_svc_17y", hash = "cb2921e7", zone = 1)
    public Drive getDriveById(String id) {
        beforeGetDriveById(id);
        try {
            return driveRepository.findById(id).orElseThrow(() -> new NotFoundException("Drive not found with id: " + id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetDriveById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_drive_by_id_svc_17y", hash = "67c0d3c6", zone = 2)
    public void beforeGetDriveById(String id) {
    }

    @MethodMetadata(irId = "hook-after-get_drive_by_id_svc_17y", hash = "af5d8a74", zone = 2)
    public void afterGetDriveById(String id) {
    }

    @MethodMetadata(irId = "delete_drive_svc_s9r", hash = "63adfdc8", zone = 1)
    public void deleteDrive(String id) {
        beforeDeleteDrive(id);
        try {
            driveRepository.findById(id).orElseThrow(() -> new NotFoundException("Drive not found with id: " + id));
            driveRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteDrive(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_drive_svc_s9r", hash = "aa5f24dd", zone = 2)
    public void beforeDeleteDrive(String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_drive_svc_s9r", hash = "6dbddad3", zone = 2)
    public void afterDeleteDrive(String id) {
    }

    @Autowired()
    private DriveRepository driveRepository;

    /*
 * Operation    : List Active Admission Drives
 * Usecase ID   : UC-05
 * Usecase Name : View Active Admission Drives
 */
    @MethodMetadata(irId = "listActiveDrives_m9h", hash = "2daced74", zone = 1)
    public List<Drive> listActiveDrives() {
        LocalDate today = LocalDate.now();
        List<Drive> allDrives = driveRepository.findAll();
        return allDrives.stream().filter(d -> d.getEditStartDate() != null && d.getEditEndDate() != null && !today.isBefore(d.getEditStartDate()) && !today.isAfter(d.getEditEndDate())).collect(Collectors.toList());
    }

    @Autowired()
    private KeycloakAuthService keycloakAuthService;

    /*
 * Operation    : Create Admission Drive
 * Usecase ID   : UC-09
 * Usecase Name : Create Admission Drive
 */
    @MethodMetadata(irId = "createDrive_1b9", hash = "f939c4b6", zone = 1)
    public String createDrive(CreateDriveRequest driveRequest, Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        // Validate Admin role (already enforced via required_roles, but double-check if needed)
        // No user lookup needed for admin-only write operation unless audit is required — omitted per spec.
        Drive drive = new Drive();
        drive.setId(UUID.randomUUID().toString());
        drive.setName(driveRequest.getName());
        drive.setDriveType(driveRequest.getDriveType());
        drive.setStartDate(driveRequest.getStartDate());
        drive.setEndDate(driveRequest.getEndDate());
        drive.setEditStartDate(driveRequest.getEditStartDate());
        drive.setEditEndDate(driveRequest.getEditEndDate());
        drive.setJeeEditingEndDate(driveRequest.getJeeEditingEndDate());
        drive.setDetails(driveRequest.getDetails());
        // Validate end date is after start date
        if (drive.getEndDate().isBefore(drive.getStartDate()) || drive.getEndDate().equals(drive.getStartDate())) {
            throw new RuntimeException("End date must be later than start date.");
        }
        driveRepository.save(drive);
        return drive.getId();
    }

    /*
 * Operation    : Edit Drive Configuration
 * Usecase ID   : UC-10
 * Usecase Name : Edit Admission Drive Configuration
 */
    @MethodMetadata(irId = "updateDriveConfiguration_pqz", hash = "a619018c", zone = 1)
    public Void updateDriveConfiguration(String id, UpdateDriveRequest request) {
        Drive drive = driveRepository.findById(id).orElseThrow(() -> new RuntimeException("Drive not found"));
        if (request.getEndDate() != null && request.getStartDate() != null) {
            if (!request.getEndDate().isAfter(request.getStartDate())) {
                throw new RuntimeException("End date must be after start date");
            }
        }
        if (request.getName() != null)
            drive.setName(request.getName());
        if (request.getDriveType() != null)
            drive.setDriveType(request.getDriveType());
        if (request.getStartDate() != null)
            drive.setStartDate(request.getStartDate());
        if (request.getEndDate() != null)
            drive.setEndDate(request.getEndDate());
        if (request.getEditStartDate() != null)
            drive.setEditStartDate(request.getEditStartDate());
        if (request.getEditEndDate() != null)
            drive.setEditEndDate(request.getEditEndDate());
        if (request.getJeeEditingEndDate() != null)
            drive.setJeeEditingEndDate(request.getJeeEditingEndDate());
        if (request.getDetails() != null)
            drive.setDetails(request.getDetails());
        driveRepository.save(drive);
        return null;
    }
}
