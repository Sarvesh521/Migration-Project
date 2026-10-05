package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateApplicationEducationRequest;
import com.example.app.dto.UpdateApplicationEducationRequest;
import com.example.app.entity.ApplicationEducation;
import com.example.app.service.ApplicationEducationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/application-educations")
public class ApplicationEducationController {

    @Autowired()
    private ApplicationEducationService applicationEducationService;

    @MethodMetadata(irId = "create_application_education_ctrl_pyz", hash = "45690728", zone = 1)
    @PostMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationEducation> createApplicationEducation(@RequestParam(required = false) CreateApplicationEducationRequest request) {
        beforeCreateApplicationEducation(request);
        try {
            return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationEducationService.createApplicationEducation(request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationEducation(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_education_ctrl_pyz", hash = "33a2c433", zone = 2)
    public void beforeCreateApplicationEducation(@RequestParam(required = false) CreateApplicationEducationRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_education_ctrl_pyz", hash = "196b29ed", zone = 2)
    public void afterCreateApplicationEducation(@RequestParam(required = false) CreateApplicationEducationRequest request) {
    }

    @MethodMetadata(irId = "get_application_education_by_id_ctrl_sj6", hash = "f949869e", zone = 1)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationEducation> getApplicationEducationById(@PathVariable String id) {
        beforeGetApplicationEducationById(id);
        try {
            return ResponseEntity.ok(applicationEducationService.getApplicationEducationById(id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationEducationById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_education_by_id_ctrl_sj6", hash = "199f703f", zone = 2)
    public void beforeGetApplicationEducationById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_education_by_id_ctrl_sj6", hash = "449d0405", zone = 2)
    public void afterGetApplicationEducationById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "list_application_educations_ctrl_nq6", hash = "107cde41", zone = 1)
    @GetMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<List<ApplicationEducation>> listApplicationEducations() {
        beforeListApplicationEducations();
        try {
            return ResponseEntity.ok(applicationEducationService.listApplicationEducations());
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationEducations();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_educations_ctrl_nq6", hash = "e00102a1", zone = 2)
    public void beforeListApplicationEducations() {
    }

    @MethodMetadata(irId = "hook-after-list_application_educations_ctrl_nq6", hash = "afef07db", zone = 2)
    public void afterListApplicationEducations() {
    }

    @MethodMetadata(irId = "update_application_education_ctrl_7em", hash = "cbfa76d7", zone = 1)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationEducation> updateApplicationEducation(@PathVariable String id, @RequestParam(required = false) UpdateApplicationEducationRequest request) {
        beforeUpdateApplicationEducation(id, request);
        try {
            return ResponseEntity.ok(applicationEducationService.updateApplicationEducation(id, request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateApplicationEducation(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_application_education_ctrl_7em", hash = "19185da6", zone = 2)
    public void beforeUpdateApplicationEducation(@PathVariable String id, @RequestParam(required = false) UpdateApplicationEducationRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_application_education_ctrl_7em", hash = "232a964a", zone = 2)
    public void afterUpdateApplicationEducation(@PathVariable String id, @RequestParam(required = false) UpdateApplicationEducationRequest request) {
    }

    @MethodMetadata(irId = "delete_application_education_ctrl_ypv", hash = "03f85a0b", zone = 1)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Void> deleteApplicationEducation(@PathVariable String id) {
        beforeDeleteApplicationEducation(id);
        try {
            applicationEducationService.deleteApplicationEducation(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationEducation(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_education_ctrl_ypv", hash = "0b965072", zone = 2)
    public void beforeDeleteApplicationEducation(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_education_ctrl_ypv", hash = "b768683d", zone = 2)
    public void afterDeleteApplicationEducation(@PathVariable String id) {
    }
}
