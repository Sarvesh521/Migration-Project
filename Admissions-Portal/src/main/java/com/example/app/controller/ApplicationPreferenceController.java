package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateApplicationPreferenceRequest;
import com.example.app.dto.UpdateApplicationPreferenceRequest;
import com.example.app.entity.ApplicationPreference;
import com.example.app.service.ApplicationPreferenceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/application-preferences")
public class ApplicationPreferenceController {

    @Autowired()
    private ApplicationPreferenceService applicationPreferenceService;

    @MethodMetadata(irId = "create_application_preference_ctrl_v1b", hash = "55d68e1f", zone = 1)
    @PostMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationPreference> createApplicationPreference(@RequestParam(required = false) CreateApplicationPreferenceRequest request) {
        beforeCreateApplicationPreference(request);
        try {
            return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationPreferenceService.createApplicationPreference(request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationPreference(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_preference_ctrl_v1b", hash = "f23d3c18", zone = 2)
    public void beforeCreateApplicationPreference(@RequestParam(required = false) CreateApplicationPreferenceRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_preference_ctrl_v1b", hash = "c4bcfec4", zone = 2)
    public void afterCreateApplicationPreference(@RequestParam(required = false) CreateApplicationPreferenceRequest request) {
    }

    @MethodMetadata(irId = "get_application_preference_by_id_ctrl_2uu", hash = "dcf323c3", zone = 1)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationPreference> getApplicationPreferenceById(@PathVariable String id) {
        beforeGetApplicationPreferenceById(id);
        try {
            return ResponseEntity.ok(applicationPreferenceService.getApplicationPreferenceById(id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationPreferenceById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_preference_by_id_ctrl_2uu", hash = "8727f5ff", zone = 2)
    public void beforeGetApplicationPreferenceById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_preference_by_id_ctrl_2uu", hash = "1a9bfed4", zone = 2)
    public void afterGetApplicationPreferenceById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "list_application_preferences_ctrl_5l4", hash = "344c76f4", zone = 1)
    @GetMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<List<ApplicationPreference>> listApplicationPreferences() {
        beforeListApplicationPreferences();
        try {
            return ResponseEntity.ok(applicationPreferenceService.listApplicationPreferences());
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationPreferences();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_preferences_ctrl_5l4", hash = "4a29a456", zone = 2)
    public void beforeListApplicationPreferences() {
    }

    @MethodMetadata(irId = "hook-after-list_application_preferences_ctrl_5l4", hash = "b4f0317d", zone = 2)
    public void afterListApplicationPreferences() {
    }

    @MethodMetadata(irId = "update_application_preference_ctrl_j4i", hash = "06e05de0", zone = 1)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationPreference> updateApplicationPreference(@PathVariable String id, @RequestParam(required = false) UpdateApplicationPreferenceRequest request) {
        beforeUpdateApplicationPreference(id, request);
        try {
            return ResponseEntity.ok(applicationPreferenceService.updateApplicationPreference(id, request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateApplicationPreference(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_application_preference_ctrl_j4i", hash = "b5864d1f", zone = 2)
    public void beforeUpdateApplicationPreference(@PathVariable String id, @RequestParam(required = false) UpdateApplicationPreferenceRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_application_preference_ctrl_j4i", hash = "759be1db", zone = 2)
    public void afterUpdateApplicationPreference(@PathVariable String id, @RequestParam(required = false) UpdateApplicationPreferenceRequest request) {
    }

    @MethodMetadata(irId = "delete_application_preference_ctrl_8uq", hash = "79312e55", zone = 1)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Void> deleteApplicationPreference(@PathVariable String id) {
        beforeDeleteApplicationPreference(id);
        try {
            applicationPreferenceService.deleteApplicationPreference(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationPreference(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_preference_ctrl_8uq", hash = "71517e74", zone = 2)
    public void beforeDeleteApplicationPreference(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_preference_ctrl_8uq", hash = "8f56ae3d", zone = 2)
    public void afterDeleteApplicationPreference(@PathVariable String id) {
    }
}
