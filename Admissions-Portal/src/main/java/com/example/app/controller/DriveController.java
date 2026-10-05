package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.*;
import com.example.app.entity.*;
import com.example.app.service.DriveService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/drives")
public class DriveController {

    @Autowired()
    private DriveService driveService;

    @MethodMetadata(irId = "get_drive_by_id_ctrl_mo5", hash = "1269db84", zone = 1)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Drive> getDriveById(@PathVariable String id) {
        beforeGetDriveById(id);
        try {
            return ResponseEntity.ok(driveService.getDriveById(id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetDriveById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_drive_by_id_ctrl_mo5", hash = "f8d6fe8b", zone = 2)
    public void beforeGetDriveById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-get_drive_by_id_ctrl_mo5", hash = "c1732408", zone = 2)
    public void afterGetDriveById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "delete_drive_ctrl_698", hash = "ddb87a83", zone = 1)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Void> deleteDrive(@PathVariable String id) {
        beforeDeleteDrive(id);
        try {
            driveService.deleteDrive(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteDrive(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_drive_ctrl_698", hash = "6041bfbb", zone = 2)
    public void beforeDeleteDrive(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_drive_ctrl_698", hash = "4747e79a", zone = 2)
    public void afterDeleteDrive(@PathVariable String id) {
    }

    /*
 * Operation    : List Active Admission Drives
 * Usecase ID   : UC-05
 * Usecase Name : View Active Admission Drives
 */
    @MethodMetadata(irId = "listActiveDrives_xer", hash = "a5fd65e2", zone = 1)
    @GetMapping(value = "/active")
    public ResponseEntity<List<Drive>> listActiveDrives() {
        return ResponseEntity.ok(driveService.listActiveDrives());
    }

    /*
 * Operation    : Create Admission Drive
 * Usecase ID   : UC-09
 * Usecase Name : Create Admission Drive
 */
    @MethodMetadata(irId = "createDrive_vyk", hash = "a09af860", zone = 1)
    @PostMapping(value = "")
    public ResponseEntity<String> createDrive(@RequestBody @Valid CreateDriveRequest driveRequest, Principal principal) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(driveService.createDrive(driveRequest, principal));
    }

    /*
 * Operation    : Edit Drive Configuration
 * Usecase ID   : UC-10
 * Usecase Name : Edit Admission Drive Configuration
 */
    @MethodMetadata(irId = "updateDriveConfiguration_ng2", hash = "004da6a2", zone = 1)
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateDriveConfiguration(@PathVariable String id, @RequestBody @Valid UpdateDriveRequest request) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(driveService.updateDriveConfiguration(id, request));
    }
}
