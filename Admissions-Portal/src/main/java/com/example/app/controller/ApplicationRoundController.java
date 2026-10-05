package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.*;
import com.example.app.entity.*;
import com.example.app.service.ApplicationRoundService;
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
@RequestMapping("/api/application-rounds")
public class ApplicationRoundController {

    @Autowired()
    private ApplicationRoundService applicationRoundService;

    @MethodMetadata(irId = "create_application_round_ctrl_tpm", hash = "6422d420", zone = 1)
    @PostMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationRound> createApplicationRound(@RequestParam(required = false) CreateApplicationRoundRequest request) {
        beforeCreateApplicationRound(request);
        try {
            return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationRoundService.createApplicationRound(request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationRound(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_round_ctrl_tpm", hash = "a4447d3c", zone = 2)
    public void beforeCreateApplicationRound(@RequestParam(required = false) CreateApplicationRoundRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_round_ctrl_tpm", hash = "49fc4659", zone = 2)
    public void afterCreateApplicationRound(@RequestParam(required = false) CreateApplicationRoundRequest request) {
    }

    @MethodMetadata(irId = "get_application_round_by_id_ctrl_wwh", hash = "1d08c393", zone = 1)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationRound> getApplicationRoundById(@PathVariable String id) {
        beforeGetApplicationRoundById(id);
        try {
            return ResponseEntity.ok(applicationRoundService.getApplicationRoundById(id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationRoundById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_round_by_id_ctrl_wwh", hash = "8c4cd14e", zone = 2)
    public void beforeGetApplicationRoundById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_round_by_id_ctrl_wwh", hash = "4d1c3c93", zone = 2)
    public void afterGetApplicationRoundById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "list_application_rounds_ctrl_n2v", hash = "f3002d2e", zone = 1)
    @GetMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<List<ApplicationRound>> listApplicationRounds() {
        beforeListApplicationRounds();
        try {
            return ResponseEntity.ok(applicationRoundService.listApplicationRounds());
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationRounds();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_rounds_ctrl_n2v", hash = "77d24fb7", zone = 2)
    public void beforeListApplicationRounds() {
    }

    @MethodMetadata(irId = "hook-after-list_application_rounds_ctrl_n2v", hash = "908cf130", zone = 2)
    public void afterListApplicationRounds() {
    }

    @MethodMetadata(irId = "update_application_round_ctrl_am2", hash = "97fa3a76", zone = 1)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationRound> updateApplicationRound(@PathVariable String id, @RequestParam(required = false) UpdateApplicationRoundRequest request) {
        beforeUpdateApplicationRound(id, request);
        try {
            return ResponseEntity.ok(applicationRoundService.updateApplicationRound(id, request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateApplicationRound(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_application_round_ctrl_am2", hash = "e6c86671", zone = 2)
    public void beforeUpdateApplicationRound(@PathVariable String id, @RequestParam(required = false) UpdateApplicationRoundRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_application_round_ctrl_am2", hash = "32e16f20", zone = 2)
    public void afterUpdateApplicationRound(@PathVariable String id, @RequestParam(required = false) UpdateApplicationRoundRequest request) {
    }

    @MethodMetadata(irId = "delete_application_round_ctrl_61n", hash = "0599595b", zone = 1)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Void> deleteApplicationRound(@PathVariable String id) {
        beforeDeleteApplicationRound(id);
        try {
            applicationRoundService.deleteApplicationRound(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationRound(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_round_ctrl_61n", hash = "2b95ddec", zone = 2)
    public void beforeDeleteApplicationRound(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_round_ctrl_61n", hash = "abd4d8fa", zone = 2)
    public void afterDeleteApplicationRound(@PathVariable String id) {
    }

    /*
 * Operation    : Process sliding allocation round
 * Usecase ID   : UC-14
 * Usecase Name : Process Offline Sliding Allocation (B.Tech/IM.Tech)
 */
    @MethodMetadata(irId = "processSlidingAllocationRound_yhc", hash = "56530233", zone = 1)
    @PostMapping(value = "/{roundId}/sliding/process")
    public ResponseEntity<Void> processSlidingAllocationRound(@PathVariable String roundId) {
        applicationRoundService.processSlidingAllocationRound(roundId);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).build();
    }

    /*
 * Operation    : Invite Candidates to Online Round
 * Usecase ID   : UC-15
 * Usecase Name : Process Online Round Registration (M.Tech)
 */
    @MethodMetadata(irId = "inviteCandidatesToOnlineRound_zgm", hash = "8951f732", zone = 1)
    @PostMapping(value = "/{roundId}/candidates/invite")
    public ResponseEntity<List<ApplicationRoundResponse>> inviteCandidatesToOnlineRound(@RequestBody @Valid InviteCandidatesRequest request, Principal principal) {
        List<ApplicationRoundResponse> responses = applicationRoundService
                .inviteCandidatesToOnlineRound(request.getApplicationIds(), request.getRoundId(), principal)
                .stream()
                .map(this::toApplicationRoundResponse)
                .toList();
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(responses);
    }

    private ApplicationRoundResponse toApplicationRoundResponse(ApplicationRound applicationRound) {
        ApplicationRoundResponse response = new ApplicationRoundResponse();
        response.setId(applicationRound.getId());
        response.setApplicationId(applicationRound.getApplicationId());
        response.setRoundId(applicationRound.getRoundId());
        response.setAllocatedProgramme(applicationRound.getAllocatedProgramme());
        response.setOfferStatus(applicationRound.getOfferStatus());
        response.setRoundStatus(applicationRound.getRoundStatus());
        response.setSlidingStatus(applicationRound.getSlidingStatus());
        response.setToken(applicationRound.getToken());
        response.setPaymentStatus(applicationRound.getPaymentStatus());
        response.setOfferLetterDocId(applicationRound.getOfferLetterDocId());
        return response;
    }

    /*
 * Operation    : Register for Online Round
 * Usecase ID   : UC-15
 * Usecase Name : Process Online Round Registration (M.Tech)
 */
    @MethodMetadata(irId = "registerForOnlineRound_cxe", hash = "60c84e97", zone = 1)
    @PostMapping(value = "/{applicationRoundId}/online-register")
    public ResponseEntity<Void> registerForOnlineRound(@PathVariable String applicationRoundId, Principal principal) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationRoundService.registerForOnlineRound(applicationRoundId, principal));
    }

    /*
 * Operation    : Generate and Send Token for Online Round
 * Usecase ID   : UC-15
 * Usecase Name : Process Online Round Registration (M.Tech)
 */
    @MethodMetadata(irId = "generateAndSendTokenForOnlineRound_0z9", hash = "9f443957", zone = 1)
    @PostMapping(value = "/{id}/token/generate-and-send")
    public ResponseEntity<Void> generateAndSendTokenForOnlineRound(@PathVariable String id) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationRoundService.generateAndSendTokenForOnlineRound(id));
    }

    /*
 * Operation    : Allocate Online Round Seat
 * Usecase ID   : UC-15
 * Usecase Name : Process Online Round Registration (M.Tech)
 */
    @MethodMetadata(irId = "allocateOnlineRoundSeat_47s", hash = "804ca26c", zone = 1)
    @PostMapping(value = "/{applicationId}/rounds/{roundId}/allocate")
    public ResponseEntity<Void> allocateOnlineRoundSeat(@PathVariable String applicationId, @PathVariable String roundId, String allocatedProgramme) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationRoundService.allocateOnlineRoundSeat(applicationId, roundId, allocatedProgramme));
    }

    /*
 * Operation    : Cancel Offer Letter
 * Usecase ID   : UC-17
 * Usecase Name : Cancel Offer Letter
 */
    @MethodMetadata(irId = "cancelOfferLetter_ukw", hash = "c3d267cc", zone = 1)
    @PostMapping(value = "/{applicationId}/{roundId}/cancel")
    public ResponseEntity<Void> cancelOfferLetter(@PathVariable String applicationId, @PathVariable String roundId, String cancellationReason) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationRoundService.cancelOfferLetter(applicationId, roundId, cancellationReason));
    }
}
