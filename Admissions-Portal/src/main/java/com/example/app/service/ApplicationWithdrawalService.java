package com.example.app.service;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.*;
import com.example.app.entity.*;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.ApplicationRepository;
import com.example.app.repository.ApplicationWithdrawalRepository;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service()
public class ApplicationWithdrawalService {

    @Autowired()
    private ApplicationRepository applicationRepository;

    @MethodMetadata(irId = "create_application_withdrawal_svc_zgi", hash = "1b317671", zone = 1)
    public ApplicationWithdrawal createApplicationWithdrawal(CreateApplicationWithdrawalRequest request) {
        beforeCreateApplicationWithdrawal(request);
        try {
            Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
            ApplicationWithdrawal applicationWithdrawal = new ApplicationWithdrawal();
            applicationWithdrawal.setName(request.getName());
            applicationWithdrawal.setEmail(request.getEmail());
            applicationWithdrawal.setContact(request.getContact());
            applicationWithdrawal.setDob(request.getDob());
            applicationWithdrawal.setAddress(request.getAddress());
            applicationWithdrawal.setProgramme(request.getProgramme());
            applicationWithdrawal.setYear(request.getYear());
            applicationWithdrawal.setReason(request.getReason());
            applicationWithdrawal.setApplication(application);
            return applicationWithdrawalRepository.save(applicationWithdrawal);
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationWithdrawal(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_withdrawal_svc_zgi", hash = "8f6d07c4", zone = 2)
    public void beforeCreateApplicationWithdrawal(CreateApplicationWithdrawalRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_withdrawal_svc_zgi", hash = "0368a08a", zone = 2)
    public void afterCreateApplicationWithdrawal(CreateApplicationWithdrawalRequest request) {
    }

    @MethodMetadata(irId = "get_application_withdrawal_by_id_svc_e5p", hash = "a569c4d4", zone = 1)
    public ApplicationWithdrawal getApplicationWithdrawalById(String id) {
        beforeGetApplicationWithdrawalById(id);
        try {
            return applicationWithdrawalRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationWithdrawal not found with id: " + id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationWithdrawalById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_withdrawal_by_id_svc_e5p", hash = "de05ea0c", zone = 2)
    public void beforeGetApplicationWithdrawalById(String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_withdrawal_by_id_svc_e5p", hash = "faa1e06a", zone = 2)
    public void afterGetApplicationWithdrawalById(String id) {
    }

    @MethodMetadata(irId = "list_application_withdrawals_svc_ohs", hash = "5274042c", zone = 1)
    public List<ApplicationWithdrawal> listApplicationWithdrawals() {
        beforeListApplicationWithdrawals();
        try {
            return applicationWithdrawalRepository.findAll();
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationWithdrawals();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_withdrawals_svc_ohs", hash = "462c00e4", zone = 2)
    public void beforeListApplicationWithdrawals() {
    }

    @MethodMetadata(irId = "hook-after-list_application_withdrawals_svc_ohs", hash = "bb5256b5", zone = 2)
    public void afterListApplicationWithdrawals() {
    }

    @MethodMetadata(irId = "delete_application_withdrawal_svc_s5v", hash = "908b2da8", zone = 1)
    public void deleteApplicationWithdrawal(String id) {
        beforeDeleteApplicationWithdrawal(id);
        try {
            applicationWithdrawalRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationWithdrawal not found with id: " + id));
            applicationWithdrawalRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationWithdrawal(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_withdrawal_svc_s5v", hash = "72cc198e", zone = 2)
    public void beforeDeleteApplicationWithdrawal(String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_withdrawal_svc_s5v", hash = "af983789", zone = 2)
    public void afterDeleteApplicationWithdrawal(String id) {
    }

    @Autowired()
    private ApplicationWithdrawalRepository applicationWithdrawalRepository;

    @Autowired()
    private KeycloakAuthService keycloakAuthService;

    /*
 * Operation    : Submit Application Withdrawal
 * Usecase ID   : UC-08
 * Usecase Name : Submit Withdrawal Request
 */
    @MethodMetadata(irId = "submitWithdrawalRequest_54n", hash = "870a62f8", zone = 1)
    public String submitWithdrawalRequest(SubmitWithdrawalRequest request, Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new RuntimeException("Application not found"));
        if (!userId.equals(application.getUserId())) {
            throw new RuntimeException("Application does not belong to the current user");
        }
        String status = application.getStatus();
        if (!"SUBMITTED".equals(status)) {
            throw new RuntimeException("Application must be in SUBMITTED state to allow withdrawal");
        }
        ApplicationWithdrawal withdrawal = new ApplicationWithdrawal();
        withdrawal.setId(java.util.UUID.randomUUID().toString());
        withdrawal.setApplicationId(request.getApplicationId());
        withdrawal.setName(application.getFullName());
        withdrawal.setEmail(application.getEmail());
        withdrawal.setContact(application.getMobile());
        withdrawal.setDob(application.getDob());
        withdrawal.setAddress(application.getCurrentAddressLine1() + ", " + application.getCurrentCity() + ", " + application.getCurrentState() + " - " + application.getCurrentPincode());
        withdrawal.setProgramme(request.getProgramme());
        withdrawal.setYear(request.getYear());
        withdrawal.setReason(request.getReason());
        applicationWithdrawalRepository.save(withdrawal);
        return withdrawal.getId();
    }
}
