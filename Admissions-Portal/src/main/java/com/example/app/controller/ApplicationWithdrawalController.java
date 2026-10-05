package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.*;
import com.example.app.entity.*;
import com.example.app.service.ApplicationWithdrawalService;
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
@RequestMapping("/api/application-withdrawals")
public class ApplicationWithdrawalController {

    @Autowired()
    private ApplicationWithdrawalService applicationWithdrawalService;

    @MethodMetadata(irId = "create_application_withdrawal_ctrl_zbm", hash = "22a3c4de", zone = 1)
    @PostMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationWithdrawal> createApplicationWithdrawal(@RequestParam(required = false) CreateApplicationWithdrawalRequest request) {
        beforeCreateApplicationWithdrawal(request);
        try {
            return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationWithdrawalService.createApplicationWithdrawal(request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationWithdrawal(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_withdrawal_ctrl_zbm", hash = "81b5c7ff", zone = 2)
    public void beforeCreateApplicationWithdrawal(@RequestParam(required = false) CreateApplicationWithdrawalRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_withdrawal_ctrl_zbm", hash = "7cf29537", zone = 2)
    public void afterCreateApplicationWithdrawal(@RequestParam(required = false) CreateApplicationWithdrawalRequest request) {
    }

    @MethodMetadata(irId = "get_application_withdrawal_by_id_ctrl_3vp", hash = "29721b41", zone = 1)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationWithdrawal> getApplicationWithdrawalById(@PathVariable String id) {
        beforeGetApplicationWithdrawalById(id);
        try {
            return ResponseEntity.ok(applicationWithdrawalService.getApplicationWithdrawalById(id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationWithdrawalById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_withdrawal_by_id_ctrl_3vp", hash = "e86f853d", zone = 2)
    public void beforeGetApplicationWithdrawalById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_withdrawal_by_id_ctrl_3vp", hash = "187d6f18", zone = 2)
    public void afterGetApplicationWithdrawalById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "list_application_withdrawals_ctrl_r2p", hash = "25c2cd95", zone = 1)
    @GetMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<List<ApplicationWithdrawal>> listApplicationWithdrawals() {
        beforeListApplicationWithdrawals();
        try {
            return ResponseEntity.ok(applicationWithdrawalService.listApplicationWithdrawals());
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationWithdrawals();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_withdrawals_ctrl_r2p", hash = "462c00e4", zone = 2)
    public void beforeListApplicationWithdrawals() {
    }

    @MethodMetadata(irId = "hook-after-list_application_withdrawals_ctrl_r2p", hash = "bb5256b5", zone = 2)
    public void afterListApplicationWithdrawals() {
    }

    @MethodMetadata(irId = "delete_application_withdrawal_ctrl_0z6", hash = "689f7b16", zone = 1)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Void> deleteApplicationWithdrawal(@PathVariable String id) {
        beforeDeleteApplicationWithdrawal(id);
        try {
            applicationWithdrawalService.deleteApplicationWithdrawal(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationWithdrawal(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_withdrawal_ctrl_0z6", hash = "026fbd86", zone = 2)
    public void beforeDeleteApplicationWithdrawal(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_withdrawal_ctrl_0z6", hash = "f256ec8c", zone = 2)
    public void afterDeleteApplicationWithdrawal(@PathVariable String id) {
    }

    /*
 * Operation    : Submit Application Withdrawal
 * Usecase ID   : UC-08
 * Usecase Name : Submit Withdrawal Request
 */
    @MethodMetadata(irId = "submitWithdrawalRequest_3dw", hash = "3b2dc4f8", zone = 1)
    @PostMapping(value = "/withdrawals")
    public ResponseEntity<String> submitWithdrawalRequest(@RequestBody @Valid SubmitWithdrawalRequest request, Principal principal) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationWithdrawalService.submitWithdrawalRequest(request, principal));
    }
}
