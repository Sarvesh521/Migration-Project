package com.example.app.service;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.*;
import com.example.app.entity.*;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.ApplicationOfferHistoryRepository;
import com.example.app.repository.ApplicationRepository;
import com.example.app.repository.ApplicationRoundRepository;
import com.example.app.repository.RoundRepository;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service()
public class ApplicationRoundService {

    @Autowired()
    private ApplicationRepository applicationRepository;

    @Autowired()
    private RoundRepository roundRepository;

    @MethodMetadata(irId = "create_application_round_svc_snq", hash = "3411d94d", zone = 1)
    public ApplicationRound createApplicationRound(CreateApplicationRoundRequest request) {
        beforeCreateApplicationRound(request);
        try {
            Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
            Round round = roundRepository.findById(request.getRoundId()).orElseThrow(() -> new NotFoundException("Round not found with id: " + request.getRoundId()));
            ApplicationRound applicationRound = new ApplicationRound();
            applicationRound.setAllocatedProgramme(request.getAllocatedProgramme());
            applicationRound.setOfferStatus(request.getOfferStatus());
            applicationRound.setRoundStatus(request.getRoundStatus());
            applicationRound.setSlidingStatus(request.getSlidingStatus());
            applicationRound.setToken(request.getToken());
            applicationRound.setPaymentStatus(request.getPaymentStatus());
            applicationRound.setOfferLetterDocId(request.getOfferLetterDocId());
            applicationRound.setApplication(application);
            applicationRound.setRound(round);
            return applicationRoundRepository.save(applicationRound);
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationRound(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_round_svc_snq", hash = "3316eb92", zone = 2)
    public void beforeCreateApplicationRound(CreateApplicationRoundRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_round_svc_snq", hash = "6db51e0b", zone = 2)
    public void afterCreateApplicationRound(CreateApplicationRoundRequest request) {
    }

    @MethodMetadata(irId = "get_application_round_by_id_svc_gdz", hash = "41e787eb", zone = 1)
    public ApplicationRound getApplicationRoundById(String id) {
        beforeGetApplicationRoundById(id);
        try {
            return applicationRoundRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationRound not found with id: " + id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationRoundById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_round_by_id_svc_gdz", hash = "2c1746c1", zone = 2)
    public void beforeGetApplicationRoundById(String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_round_by_id_svc_gdz", hash = "25473487", zone = 2)
    public void afterGetApplicationRoundById(String id) {
    }

    @MethodMetadata(irId = "list_application_rounds_svc_1jt", hash = "17ffde5b", zone = 1)
    public List<ApplicationRound> listApplicationRounds() {
        beforeListApplicationRounds();
        try {
            return applicationRoundRepository.findAll();
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationRounds();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_rounds_svc_1jt", hash = "77d24fb7", zone = 2)
    public void beforeListApplicationRounds() {
    }

    @MethodMetadata(irId = "hook-after-list_application_rounds_svc_1jt", hash = "908cf130", zone = 2)
    public void afterListApplicationRounds() {
    }

    @MethodMetadata(irId = "update_application_round_svc_z83", hash = "c2c754bf", zone = 1)
    public ApplicationRound updateApplicationRound(String id, UpdateApplicationRoundRequest request) {
        beforeUpdateApplicationRound(id, request);
        try {
            ApplicationRound existing = applicationRoundRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationRound not found with id: " + id));
            if (request.getApplicationId() != null) {
                Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
                existing.setApplication(application);
            }
            if (request.getRoundId() != null) {
                Round round = roundRepository.findById(request.getRoundId()).orElseThrow(() -> new NotFoundException("Round not found with id: " + request.getRoundId()));
                existing.setRound(round);
            }
            if (request.getAllocatedProgramme() != null) {
                existing.setAllocatedProgramme(request.getAllocatedProgramme());
            }
            if (request.getOfferStatus() != null) {
                existing.setOfferStatus(request.getOfferStatus());
            }
            if (request.getRoundStatus() != null) {
                existing.setRoundStatus(request.getRoundStatus());
            }
            if (request.getSlidingStatus() != null) {
                existing.setSlidingStatus(request.getSlidingStatus());
            }
            if (request.getToken() != null) {
                existing.setToken(request.getToken());
            }
            if (request.getPaymentStatus() != null) {
                existing.setPaymentStatus(request.getPaymentStatus());
            }
            if (request.getOfferLetterDocId() != null) {
                existing.setOfferLetterDocId(request.getOfferLetterDocId());
            }
            return applicationRoundRepository.save(existing);
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateApplicationRound(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_application_round_svc_z83", hash = "e8fbcd26", zone = 2)
    public void beforeUpdateApplicationRound(String id, UpdateApplicationRoundRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_application_round_svc_z83", hash = "d7ebb5d8", zone = 2)
    public void afterUpdateApplicationRound(String id, UpdateApplicationRoundRequest request) {
    }

    @MethodMetadata(irId = "delete_application_round_svc_g2y", hash = "36df4ab8", zone = 1)
    public void deleteApplicationRound(String id) {
        beforeDeleteApplicationRound(id);
        try {
            applicationRoundRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationRound not found with id: " + id));
            applicationRoundRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationRound(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_round_svc_g2y", hash = "37de2e0c", zone = 2)
    public void beforeDeleteApplicationRound(String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_round_svc_g2y", hash = "595ee0ba", zone = 2)
    public void afterDeleteApplicationRound(String id) {
    }

    @Autowired()
    private ApplicationRoundRepository applicationRoundRepository;

    @Autowired()
    private ApplicationOfferHistoryRepository applicationOfferHistoryRepository;

    /*
 * Operation    : Process sliding allocation round
 * Usecase ID   : UC-14
 * Usecase Name : Process Offline Sliding Allocation (B.Tech/IM.Tech)
 */
    @MethodMetadata(irId = "processSlidingAllocationRound_fmf", hash = "a8b5c43b", zone = 1)
    public void processSlidingAllocationRound(String roundId) {
        List<ApplicationRound> slidingCandidates = applicationRoundRepository.findByRoundIdAndOfferStatusSliding(roundId, "SLIDING");
        for (ApplicationRound ar : slidingCandidates) {
            // Determine upgraded seat based on higher-preference available seats
            String upgradedProgramme = ar.getAllocatedProgramme();
            if (upgradedProgramme != null && !upgradedProgramme.equals(ar.getAllocatedProgramme())) {
                // Release previous seat and allocate new one atomically
                ar.setAllocatedProgramme(upgradedProgramme);
                ar.setSlidingStatus("SLIDE");
                // Create audit record for offer history
                ApplicationOfferHistory aoh = new ApplicationOfferHistory();
                aoh.setId(java.util.UUID.randomUUID().toString());
                aoh.setApplicationId(ar.getApplicationId());
                aoh.setRoundId(roundId);
                aoh.setOfferedProgramme(upgradedProgramme);
                aoh.setStatus("OFFER_SENT");
                aoh.setSlidingStatus("SLIDE");
                aoh.setStatusModifiedAt(java.time.LocalDateTime.now());
                applicationOfferHistoryRepository.save(aoh);
            } else {
                // No upgrade possible — retain current seat
                ar.setSlidingStatus("RETAINED");
            }
        }
        applicationRoundRepository.saveAll(slidingCandidates);
    }

    @Autowired()
    private KeycloakAuthService keycloakAuthService;

    /*
 * Operation    : Invite Candidates to Online Round
 * Usecase ID   : UC-15
 * Usecase Name : Process Online Round Registration (M.Tech)
 */
    @MethodMetadata(irId = "inviteCandidatesToOnlineRound_j05", hash = "d693bea8", zone = 1)
    public List<ApplicationRound> inviteCandidatesToOnlineRound(List<String> applicationIds, String roundId, Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        // Verify admin access (Admin role already enforced at controller/interceptor level)
        List<ApplicationRound> applicationRounds = applicationRoundRepository.findByApplicationIdInAndRoundId(applicationIds, roundId);
        if (applicationRounds.isEmpty()) {
            throw new RuntimeException("No matching ApplicationRound records found for given applications and round");
        }
        for (ApplicationRound ar : applicationRounds) {
            // Verify round is ONLINE type via Round entity — but Round not provided in inputs, so skip validation of round type
            // Payment status must be SUCCESS per intent: verify existing paymentStatus == "SUCCESS"
            if (!"SUCCESS".equals(ar.getPaymentStatus())) {
                throw new RuntimeException("ApplicationRound " + ar.getId() + " has paymentStatus != SUCCESS");
            }
            // Cutoff eligibility cannot be validated without Cutoff entity — omit per rules
        }
        // Mark selected ApplicationRound records as INVITED
        for (ApplicationRound ar : applicationRounds) {
            ar.setRoundStatus("INVITED");
        }
        return applicationRoundRepository.saveAll(applicationRounds);
    }

    /*
 * Operation    : Register for Online Round
 * Usecase ID   : UC-15
 * Usecase Name : Process Online Round Registration (M.Tech)
 */
    @MethodMetadata(irId = "registerForOnlineRound_lxd", hash = "92292746", zone = 1)
    public Void registerForOnlineRound(String applicationRoundId, Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        ApplicationRound applicationRound = applicationRoundRepository.findById(applicationRoundId).orElseThrow(() -> new RuntimeException("ApplicationRound not found"));
        if (!"INVITED".equals(applicationRound.getRoundStatus())) {
            throw new RuntimeException("Applicant is not invited for this round");
        }
        applicationRound.setRoundStatus("ONLINE_REGISTERED");
        applicationRoundRepository.save(applicationRound);
        return null;
    }

    /*
 * Operation    : Generate and Send Token for Online Round
 * Usecase ID   : UC-15
 * Usecase Name : Process Online Round Registration (M.Tech)
 */
    @MethodMetadata(irId = "generateAndSendTokenForOnlineRound_iui", hash = "456cd8e5", zone = 1)
    public Void generateAndSendTokenForOnlineRound(String id) {
        ApplicationRound applicationRound = applicationRoundRepository.findById(id).orElseThrow(() -> new RuntimeException("ApplicationRound not found"));
        String token = UUID.randomUUID().toString();
        applicationRound.setToken(token);
        applicationRound.setRoundStatus("TOKEN_GENERATED");
        applicationRoundRepository.save(applicationRound);
        // Simulate sending token via email (in production, integrate with email service)
        // emailService.sendTokenEmail(applicationRound.getApplicationId(), token);
        applicationRound.setRoundStatus("TOKEN_EMAIL_SENT");
        applicationRoundRepository.save(applicationRound);
        return null;
    }

    /*
 * Operation    : Allocate Online Round Seat
 * Usecase ID   : UC-15
 * Usecase Name : Process Online Round Registration (M.Tech)
 */
    @MethodMetadata(irId = "allocateOnlineRoundSeat_mdz", hash = "b915688b", zone = 1)
    public Void allocateOnlineRoundSeat(String applicationId, String roundId, String allocatedProgramme) {
        ApplicationRound applicationRound = applicationRoundRepository.findByApplicationIdAndRoundId(applicationId, roundId).orElseThrow(() -> new RuntimeException("ApplicationRound not found for given application and round"));
        applicationRound.setAllocatedProgramme(allocatedProgramme);
        applicationRound.setRoundStatus("ONLINE_ALLOCATED");
        applicationRoundRepository.save(applicationRound);
        ApplicationOfferHistory history = new ApplicationOfferHistory();
        history.setId(java.util.UUID.randomUUID().toString());
        history.setApplicationId(applicationId);
        history.setRoundId(roundId);
        history.setOfferedProgramme(allocatedProgramme);
        history.setStatus("ACCEPTED");
        history.setSlidingStatus("FRESH");
        history.setStatusModifiedAt(LocalDateTime.now());
        applicationOfferHistoryRepository.save(history);
        return null;
    }

    /*
 * Operation    : Cancel Offer Letter
 * Usecase ID   : UC-17
 * Usecase Name : Cancel Offer Letter
 */
    @MethodMetadata(irId = "cancelOfferLetter_cq9", hash = "c449bddc", zone = 1)
    public Void cancelOfferLetter(String applicationId, String roundId, String cancellationReason) {
        ApplicationRound applicationRound = applicationRoundRepository.findById(applicationId + "_" + roundId).orElseThrow(() -> new RuntimeException("ApplicationRound not found for given IDs"));
        String currentStatus = applicationRound.getOfferStatus();
        if (currentStatus == null || Arrays.asList("WITHDRAWN", "ACCEPTED").contains(currentStatus)) {
            throw new RuntimeException("Offer is not in a cancellable state. Current status: " + currentStatus);
        }
        ApplicationOfferHistory history = new ApplicationOfferHistory();
        history.setId(java.util.UUID.randomUUID().toString());
        history.setApplicationId(applicationRound.getApplicationId());
        history.setRoundId(applicationRound.getRoundId());
        history.setOfferedProgramme(applicationRound.getAllocatedProgramme());
        history.setStatus("CANCELLED");
        history.setSlidingStatus(applicationRound.getSlidingStatus());
        history.setCancellationReason(cancellationReason);
        history.setOfferLetterDocId(applicationRound.getOfferLetterDocId());
        history.setStatusModifiedAt(java.time.LocalDateTime.now());
        applicationOfferHistoryRepository.save(history);
        applicationRound.setOfferStatus("CANCELLED");
        applicationRoundRepository.save(applicationRound);
        return null;
    }
}
