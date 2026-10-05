package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateApplicationPaymentRequest;
import com.example.app.dto.UpdateApplicationPaymentRequest;
import com.example.app.entity.ApplicationPayment;
import com.example.app.service.ApplicationPaymentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/application-payments")
public class ApplicationPaymentController {

    @Autowired()
    private ApplicationPaymentService applicationPaymentService;

    @MethodMetadata(irId = "create_application_payment_ctrl_plx", hash = "962771af", zone = 1)
    @PostMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationPayment> createApplicationPayment(@RequestParam(required = false) CreateApplicationPaymentRequest request) {
        beforeCreateApplicationPayment(request);
        try {
            return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationPaymentService.createApplicationPayment(request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationPayment(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_payment_ctrl_plx", hash = "d7d6278f", zone = 2)
    public void beforeCreateApplicationPayment(@RequestParam(required = false) CreateApplicationPaymentRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_payment_ctrl_plx", hash = "638a4fc5", zone = 2)
    public void afterCreateApplicationPayment(@RequestParam(required = false) CreateApplicationPaymentRequest request) {
    }

    @MethodMetadata(irId = "get_application_payment_by_id_ctrl_los", hash = "2430a35a", zone = 1)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationPayment> getApplicationPaymentById(@PathVariable String id) {
        beforeGetApplicationPaymentById(id);
        try {
            return ResponseEntity.ok(applicationPaymentService.getApplicationPaymentById(id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationPaymentById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_payment_by_id_ctrl_los", hash = "aeef25bb", zone = 2)
    public void beforeGetApplicationPaymentById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_payment_by_id_ctrl_los", hash = "17c8be54", zone = 2)
    public void afterGetApplicationPaymentById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "list_application_payments_ctrl_jp5", hash = "ed9ab792", zone = 1)
    @GetMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<List<ApplicationPayment>> listApplicationPayments() {
        beforeListApplicationPayments();
        try {
            return ResponseEntity.ok(applicationPaymentService.listApplicationPayments());
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationPayments();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_payments_ctrl_jp5", hash = "6ab21ba3", zone = 2)
    public void beforeListApplicationPayments() {
    }

    @MethodMetadata(irId = "hook-after-list_application_payments_ctrl_jp5", hash = "e8e4a001", zone = 2)
    public void afterListApplicationPayments() {
    }

    @MethodMetadata(irId = "update_application_payment_ctrl_18p", hash = "2a854563", zone = 1)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationPayment> updateApplicationPayment(@PathVariable String id, @RequestParam(required = false) UpdateApplicationPaymentRequest request) {
        beforeUpdateApplicationPayment(id, request);
        try {
            return ResponseEntity.ok(applicationPaymentService.updateApplicationPayment(id, request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateApplicationPayment(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_application_payment_ctrl_18p", hash = "3da45771", zone = 2)
    public void beforeUpdateApplicationPayment(@PathVariable String id, @RequestParam(required = false) UpdateApplicationPaymentRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_application_payment_ctrl_18p", hash = "6816ad87", zone = 2)
    public void afterUpdateApplicationPayment(@PathVariable String id, @RequestParam(required = false) UpdateApplicationPaymentRequest request) {
    }

    @MethodMetadata(irId = "delete_application_payment_ctrl_vno", hash = "4e8da5ba", zone = 1)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Void> deleteApplicationPayment(@PathVariable String id) {
        beforeDeleteApplicationPayment(id);
        try {
            applicationPaymentService.deleteApplicationPayment(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationPayment(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_payment_ctrl_vno", hash = "3c495d8a", zone = 2)
    public void beforeDeleteApplicationPayment(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_payment_ctrl_vno", hash = "f9cd779d", zone = 2)
    public void afterDeleteApplicationPayment(@PathVariable String id) {
    }
}
