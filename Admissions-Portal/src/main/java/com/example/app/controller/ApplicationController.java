package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.*;
import com.example.app.entity.*;
import com.example.app.service.ApplicationService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/ applications")
public class ApplicationController {

    @Autowired()
    private ApplicationService applicationService;

    /*
 * Operation    : Save Draft Application
 * Usecase ID   : UC-02
 * Usecase Name : Save Draft Application
 */
    @MethodMetadata(irId = "saveDraftApplication_vjq", hash = "8ef2f63d", zone = 1)
    @PostMapping(value = "/drafts")
    public ResponseEntity<String> saveDraftApplication(@RequestBody @Valid SaveDraftApplicationRequest request, Principal principal) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationService.saveDraftApplication(request, principal));
    }

    /*
 * Operation    : Submit Application After Payment
 * Usecase ID   : UC-03
 * Usecase Name : Submit Application After Payment
 */
    @MethodMetadata(irId = "submitApplicationAfterPayment_n8o", hash = "34dfb1d2", zone = 1)
    @PostMapping(value = "/{applicationId}/submit-after-payment")
    public ResponseEntity<Void> submitApplicationAfterPayment(@PathVariable String applicationId, Principal principal) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationService.submitApplicationAfterPayment(applicationId, principal));
    }
}
