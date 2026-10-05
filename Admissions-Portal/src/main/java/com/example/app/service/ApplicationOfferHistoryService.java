package com.example.app.service;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateApplicationOfferHistoryRequest;
import com.example.app.dto.UpdateApplicationOfferHistoryRequest;
import com.example.app.entity.ApplicationOfferHistory;
import com.example.app.entity.Application;
import com.example.app.entity.Round;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.ApplicationRepository;
import com.example.app.repository.RoundRepository;
import com.example.app.repository.ApplicationOfferHistoryRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service()
public class ApplicationOfferHistoryService {

    @Autowired()
    private ApplicationRepository applicationRepository;

    @Autowired()
    private RoundRepository roundRepository;

    @Autowired()
    private ApplicationOfferHistoryRepository applicationOfferHistoryRepository;

    @MethodMetadata(irId = "create_application_offer_history_svc_2d5", hash = "1d11ea28", zone = 1)
    public ApplicationOfferHistory createApplicationOfferHistory(CreateApplicationOfferHistoryRequest request) {
        beforeCreateApplicationOfferHistory(request);
        try {
            Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
            Round round = roundRepository.findById(request.getRoundId()).orElseThrow(() -> new NotFoundException("Round not found with id: " + request.getRoundId()));
            ApplicationOfferHistory applicationOfferHistory = new ApplicationOfferHistory();
            applicationOfferHistory.setOfferedProgramme(request.getOfferedProgramme());
            applicationOfferHistory.setStatus(request.getStatus());
            applicationOfferHistory.setSlidingStatus(request.getSlidingStatus());
            applicationOfferHistory.setCancellationReason(request.getCancellationReason());
            applicationOfferHistory.setOfferLetterDocId(request.getOfferLetterDocId());
            applicationOfferHistory.setApplication(application);
            applicationOfferHistory.setRound(round);
            return applicationOfferHistoryRepository.save(applicationOfferHistory);
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationOfferHistory(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_offer_history_svc_2d5", hash = "f9b53864", zone = 2)
    public void beforeCreateApplicationOfferHistory(CreateApplicationOfferHistoryRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_offer_history_svc_2d5", hash = "dbb09572", zone = 2)
    public void afterCreateApplicationOfferHistory(CreateApplicationOfferHistoryRequest request) {
    }

    @MethodMetadata(irId = "get_application_offer_history_by_id_svc_ief", hash = "6b0d170e", zone = 1)
    public ApplicationOfferHistory getApplicationOfferHistoryById(String id) {
        beforeGetApplicationOfferHistoryById(id);
        try {
            return applicationOfferHistoryRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationOfferHistory not found with id: " + id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationOfferHistoryById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_offer_history_by_id_svc_ief", hash = "afa9a20a", zone = 2)
    public void beforeGetApplicationOfferHistoryById(String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_offer_history_by_id_svc_ief", hash = "8935ee6c", zone = 2)
    public void afterGetApplicationOfferHistoryById(String id) {
    }

    @MethodMetadata(irId = "list_application_offer_historys_svc_w9n", hash = "69e11002", zone = 1)
    public List<ApplicationOfferHistory> listApplicationOfferHistorys() {
        beforeListApplicationOfferHistorys();
        try {
            return applicationOfferHistoryRepository.findAll();
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationOfferHistorys();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_offer_historys_svc_w9n", hash = "152e6b20", zone = 2)
    public void beforeListApplicationOfferHistorys() {
    }

    @MethodMetadata(irId = "hook-after-list_application_offer_historys_svc_w9n", hash = "adc6a1ef", zone = 2)
    public void afterListApplicationOfferHistorys() {
    }

    @MethodMetadata(irId = "update_application_offer_history_svc_uo4", hash = "5c3aec31", zone = 1)
    public ApplicationOfferHistory updateApplicationOfferHistory(String id, UpdateApplicationOfferHistoryRequest request) {
        beforeUpdateApplicationOfferHistory(id, request);
        try {
            ApplicationOfferHistory existing = applicationOfferHistoryRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationOfferHistory not found with id: " + id));
            if (request.getApplicationId() != null) {
                Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
                existing.setApplication(application);
            }
            if (request.getRoundId() != null) {
                Round round = roundRepository.findById(request.getRoundId()).orElseThrow(() -> new NotFoundException("Round not found with id: " + request.getRoundId()));
                existing.setRound(round);
            }
            if (request.getOfferedProgramme() != null) {
                existing.setOfferedProgramme(request.getOfferedProgramme());
            }
            if (request.getStatus() != null) {
                existing.setStatus(request.getStatus());
            }
            if (request.getSlidingStatus() != null) {
                existing.setSlidingStatus(request.getSlidingStatus());
            }
            if (request.getCancellationReason() != null) {
                existing.setCancellationReason(request.getCancellationReason());
            }
            if (request.getOfferLetterDocId() != null) {
                existing.setOfferLetterDocId(request.getOfferLetterDocId());
            }
            return applicationOfferHistoryRepository.save(existing);
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateApplicationOfferHistory(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_application_offer_history_svc_uo4", hash = "78f1686c", zone = 2)
    public void beforeUpdateApplicationOfferHistory(String id, UpdateApplicationOfferHistoryRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_application_offer_history_svc_uo4", hash = "d1eda3f8", zone = 2)
    public void afterUpdateApplicationOfferHistory(String id, UpdateApplicationOfferHistoryRequest request) {
    }

    @MethodMetadata(irId = "delete_application_offer_history_svc_zql", hash = "f9a4ae8c", zone = 1)
    public void deleteApplicationOfferHistory(String id) {
        beforeDeleteApplicationOfferHistory(id);
        try {
            applicationOfferHistoryRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationOfferHistory not found with id: " + id));
            applicationOfferHistoryRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationOfferHistory(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_offer_history_svc_zql", hash = "dccda79e", zone = 2)
    public void beforeDeleteApplicationOfferHistory(String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_offer_history_svc_zql", hash = "cae5d41a", zone = 2)
    public void afterDeleteApplicationOfferHistory(String id) {
    }
}
