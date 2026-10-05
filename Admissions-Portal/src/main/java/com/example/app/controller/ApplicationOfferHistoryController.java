package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateApplicationOfferHistoryRequest;
import com.example.app.dto.UpdateApplicationOfferHistoryRequest;
import com.example.app.entity.ApplicationOfferHistory;
import com.example.app.service.ApplicationOfferHistoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/application-offer-histories")
public class ApplicationOfferHistoryController {

    @Autowired()
    private ApplicationOfferHistoryService applicationOfferHistoryService;

    @MethodMetadata(irId = "create_application_offer_history_ctrl_o0j", hash = "34fa94f0", zone = 1)
    @PostMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationOfferHistory> createApplicationOfferHistory(@RequestParam(required = false) CreateApplicationOfferHistoryRequest request) {
        beforeCreateApplicationOfferHistory(request);
        try {
            return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationOfferHistoryService.createApplicationOfferHistory(request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationOfferHistory(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_offer_history_ctrl_o0j", hash = "cb95f938", zone = 2)
    public void beforeCreateApplicationOfferHistory(@RequestParam(required = false) CreateApplicationOfferHistoryRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_offer_history_ctrl_o0j", hash = "cd9f3c83", zone = 2)
    public void afterCreateApplicationOfferHistory(@RequestParam(required = false) CreateApplicationOfferHistoryRequest request) {
    }

    @MethodMetadata(irId = "get_application_offer_history_by_id_ctrl_zgc", hash = "396e8f5b", zone = 1)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationOfferHistory> getApplicationOfferHistoryById(@PathVariable String id) {
        beforeGetApplicationOfferHistoryById(id);
        try {
            return ResponseEntity.ok(applicationOfferHistoryService.getApplicationOfferHistoryById(id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationOfferHistoryById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_offer_history_by_id_ctrl_zgc", hash = "b34347e1", zone = 2)
    public void beforeGetApplicationOfferHistoryById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_offer_history_by_id_ctrl_zgc", hash = "55e1ea00", zone = 2)
    public void afterGetApplicationOfferHistoryById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "list_application_offer_historys_ctrl_msh", hash = "6fd9f98a", zone = 1)
    @GetMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<List<ApplicationOfferHistory>> listApplicationOfferHistorys() {
        beforeListApplicationOfferHistorys();
        try {
            return ResponseEntity.ok(applicationOfferHistoryService.listApplicationOfferHistorys());
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationOfferHistorys();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_offer_historys_ctrl_msh", hash = "152e6b20", zone = 2)
    public void beforeListApplicationOfferHistorys() {
    }

    @MethodMetadata(irId = "hook-after-list_application_offer_historys_ctrl_msh", hash = "adc6a1ef", zone = 2)
    public void afterListApplicationOfferHistorys() {
    }

    @MethodMetadata(irId = "update_application_offer_history_ctrl_0rl", hash = "d2d94e67", zone = 1)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationOfferHistory> updateApplicationOfferHistory(@PathVariable String id, @RequestParam(required = false) UpdateApplicationOfferHistoryRequest request) {
        beforeUpdateApplicationOfferHistory(id, request);
        try {
            return ResponseEntity.ok(applicationOfferHistoryService.updateApplicationOfferHistory(id, request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateApplicationOfferHistory(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_application_offer_history_ctrl_0rl", hash = "f9c162f8", zone = 2)
    public void beforeUpdateApplicationOfferHistory(@PathVariable String id, @RequestParam(required = false) UpdateApplicationOfferHistoryRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_application_offer_history_ctrl_0rl", hash = "1d244aa2", zone = 2)
    public void afterUpdateApplicationOfferHistory(@PathVariable String id, @RequestParam(required = false) UpdateApplicationOfferHistoryRequest request) {
    }

    @MethodMetadata(irId = "delete_application_offer_history_ctrl_sq9", hash = "3b602aca", zone = 1)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Void> deleteApplicationOfferHistory(@PathVariable String id) {
        beforeDeleteApplicationOfferHistory(id);
        try {
            applicationOfferHistoryService.deleteApplicationOfferHistory(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationOfferHistory(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_offer_history_ctrl_sq9", hash = "5db02f5b", zone = 2)
    public void beforeDeleteApplicationOfferHistory(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_offer_history_ctrl_sq9", hash = "cbc510c2", zone = 2)
    public void afterDeleteApplicationOfferHistory(@PathVariable String id) {
    }
}
