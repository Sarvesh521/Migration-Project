package com.example.app.service;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.*;
import com.example.app.entity.*;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.DriveRepository;
import com.example.app.repository.RoundRepository;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service()
public class RoundService {

    @Autowired()
    private DriveRepository driveRepository;

    @MethodMetadata(irId = "get_round_by_id_svc_n36", hash = "2fd61dc2", zone = 1)
    public Round getRoundById(String id) {
        beforeGetRoundById(id);
        try {
            return roundRepository.findById(id).orElseThrow(() -> new NotFoundException("Round not found with id: " + id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetRoundById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_round_by_id_svc_n36", hash = "2b5228b4", zone = 2)
    public void beforeGetRoundById(String id) {
    }

    @MethodMetadata(irId = "hook-after-get_round_by_id_svc_n36", hash = "77ce522f", zone = 2)
    public void afterGetRoundById(String id) {
    }

    @MethodMetadata(irId = "list_rounds_svc_un4", hash = "955fd7f7", zone = 1)
    public List<Round> listRounds() {
        beforeListRounds();
        try {
            return roundRepository.findAll();
        } catch (Exception e) {
            throw e;
        } finally {
            afterListRounds();
        }
    }

    @MethodMetadata(irId = "hook-before-list_rounds_svc_un4", hash = "e3e7cdee", zone = 2)
    public void beforeListRounds() {
    }

    @MethodMetadata(irId = "hook-after-list_rounds_svc_un4", hash = "438aebe8", zone = 2)
    public void afterListRounds() {
    }

    @MethodMetadata(irId = "update_round_svc_dh5", hash = "4ee34c33", zone = 1)
    public Round updateRound(String id, UpdateRoundRequest request) {
        beforeUpdateRound(id, request);
        try {
            Round existing = roundRepository.findById(id).orElseThrow(() -> new NotFoundException("Round not found with id: " + id));
            if (request.getDriveId() != null) {
                Drive drive = driveRepository.findById(request.getDriveId()).orElseThrow(() -> new NotFoundException("Drive not found with id: " + request.getDriveId()));
                existing.setDrive(drive);
            }
            if (request.getName() != null) {
                existing.setName(request.getName());
            }
            if (request.getRoundNumber() != null) {
                existing.setRoundNumber(request.getRoundNumber());
            }
            if (request.getRoundType() != null) {
                existing.setRoundType(request.getRoundType());
            }
            if (request.getStartDate() != null) {
                existing.setStartDate(request.getStartDate());
            }
            if (request.getEndDate() != null) {
                existing.setEndDate(request.getEndDate());
            }
            if (request.getWithdrawalEndDate() != null) {
                existing.setWithdrawalEndDate(request.getWithdrawalEndDate());
            }
            if (request.getRegistrationEndDate() != null) {
                existing.setRegistrationEndDate(request.getRegistrationEndDate());
            }
            if (request.getAllocationType() != null) {
                existing.setAllocationType(request.getAllocationType());
            }
            if (request.getAllocationMode() != null) {
                existing.setAllocationMode(request.getAllocationMode());
            }
            if (request.getFee() != null) {
                existing.setFee(request.getFee());
            }
            if (request.getOfferLetterTemplateDocId() != null) {
                existing.setOfferLetterTemplateDocId(request.getOfferLetterTemplateDocId());
            }
            if (request.getCseSeats() != null) {
                existing.setCseSeats(request.getCseSeats());
            }
            if (request.getEceSeats() != null) {
                existing.setEceSeats(request.getEceSeats());
            }
            if (request.getAiDsSeats() != null) {
                existing.setAiDsSeats(request.getAiDsSeats());
            }
            if (request.getBtechCseCutoff() != null) {
                existing.setBtechCseCutoff(request.getBtechCseCutoff());
            }
            if (request.getImtechCseCutoff() != null) {
                existing.setImtechCseCutoff(request.getImtechCseCutoff());
            }
            if (request.getMtechCseCutoff() != null) {
                existing.setMtechCseCutoff(request.getMtechCseCutoff());
            }
            if (request.getMtechEceCutoff() != null) {
                existing.setMtechEceCutoff(request.getMtechEceCutoff());
            }
            if (request.getMtechAiDsCutoff() != null) {
                existing.setMtechAiDsCutoff(request.getMtechAiDsCutoff());
            }
            return roundRepository.save(existing);
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateRound(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_round_svc_dh5", hash = "3b57c549", zone = 2)
    public void beforeUpdateRound(String id, UpdateRoundRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_round_svc_dh5", hash = "c6099fd2", zone = 2)
    public void afterUpdateRound(String id, UpdateRoundRequest request) {
    }

    @MethodMetadata(irId = "delete_round_svc_4re", hash = "7e73c08c", zone = 1)
    public void deleteRound(String id) {
        beforeDeleteRound(id);
        try {
            roundRepository.findById(id).orElseThrow(() -> new NotFoundException("Round not found with id: " + id));
            roundRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteRound(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_round_svc_4re", hash = "ed17e942", zone = 2)
    public void beforeDeleteRound(String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_round_svc_4re", hash = "ebea4d1d", zone = 2)
    public void afterDeleteRound(String id) {
    }

    @Autowired()
    private RoundRepository roundRepository;

    /*
 * Operation    : Create Admission Round with Validation
 * Usecase ID   : UC-11
 * Usecase Name : Create Admission Round
 */
    @MethodMetadata(irId = "createRoundWithValidation_i4e", hash = "ce26bef0", zone = 1)
    public String createRound(RoundRequest request, Principal principal) {
        String driveId = request.getDriveId();
        // Validate drive exists
        Drive drive = driveRepository.findById(driveId).orElseThrow(() -> new RuntimeException("Drive not found with id: " + driveId));
        // Validate round number is greater than max existing round number for this drive
        Integer maxRoundNumber = roundRepository.findMaxRoundNumberByDriveId(driveId);
        Integer requestedRoundNumber = request.getRoundNumber();
        if (maxRoundNumber != null && requestedRoundNumber <= maxRoundNumber) {
            throw new RuntimeException("Round number must be greater than the maximum existing round number for this drive.");
        }
        // Validate chronological order of dates if applicable
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new RuntimeException("Start date must be before or equal to end date.");
        }
        // Create Round entity and populate fields
        Round round = new Round();
        String id = java.util.UUID.randomUUID().toString();
        round.setId(id);
        round.setDriveId(driveId);
        round.setName(request.getName());
        round.setRoundNumber(requestedRoundNumber);
        round.setRoundType(request.getRoundType());
        round.setStartDate(startDate);
        round.setEndDate(endDate);
        round.setWithdrawalEndDate(request.getWithdrawalEndDate());
        round.setRegistrationEndDate(request.getRegistrationEndDate());
        round.setAllocationType(request.getAllocationType());
        round.setAllocationMode(request.getAllocationMode());
        round.setFee(request.getFee());
        round.setOfferLetterTemplateDocId(request.getOfferLetterTemplateDocId());
        // Initialize seat counters to zero for each seat type in the round
        Integer cseSeats = request.getCseSeats();
        Integer eceSeats = request.getEceSeats();
        Integer aiDsSeats = request.getAiDsSeats();
        round.setCseSeats(cseSeats);
        round.setEceSeats(eceSeats);
        round.setAiDsSeats(aiDsSeats);
        round.setRemainingCseSeats(0);
        round.setRemainingEceSeats(0);
        round.setRemainingAiDsSeats(0);
        // Initialize exhausted flags to false
        round.setBtcseExhausted(false);
        round.setBteceExhausted(false);
        round.setBtcseAiDsExhausted(false);
        round.setMteceExhausted(false);
        round.setMtaiDsExhausted(false);
        // Set cutoffs if provided (nullable fields)
        round.setBtechCseCutoff(request.getBtechCseCutoff());
        round.setImtechCseCutoff(request.getImtechCseCutoff());
        round.setMtechCseCutoff(request.getMtechCseCutoff());
        round.setMtechEceCutoff(request.getMtechEceCutoff());
        round.setMtechAiDsCutoff(request.getMtechAiDsCutoff());
        // Save round
        roundRepository.save(round);
        return id;
    }
}
