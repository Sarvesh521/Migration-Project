package com.example.app.service;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateApplicationPreferenceRequest;
import com.example.app.dto.UpdateApplicationPreferenceRequest;
import com.example.app.entity.ApplicationPreference;
import com.example.app.entity.Application;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.ApplicationRepository;
import com.example.app.repository.ApplicationPreferenceRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service()
public class ApplicationPreferenceService {

    @Autowired()
    private ApplicationRepository applicationRepository;

    @Autowired()
    private ApplicationPreferenceRepository applicationPreferenceRepository;

    @MethodMetadata(irId = "create_application_preference_svc_4z9", hash = "bbd04ee8", zone = 1)
    public ApplicationPreference createApplicationPreference(CreateApplicationPreferenceRequest request) {
        beforeCreateApplicationPreference(request);
        try {
            Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
            ApplicationPreference applicationPreference = new ApplicationPreference();
            applicationPreference.setPreferenceOrder(request.getPreferenceOrder());
            applicationPreference.setProgramme(request.getProgramme());
            applicationPreference.setApplication(application);
            return applicationPreferenceRepository.save(applicationPreference);
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationPreference(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_preference_svc_4z9", hash = "1ac9833b", zone = 2)
    public void beforeCreateApplicationPreference(CreateApplicationPreferenceRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_preference_svc_4z9", hash = "715a08de", zone = 2)
    public void afterCreateApplicationPreference(CreateApplicationPreferenceRequest request) {
    }

    @MethodMetadata(irId = "get_application_preference_by_id_svc_yy0", hash = "57c16c8a", zone = 1)
    public ApplicationPreference getApplicationPreferenceById(String id) {
        beforeGetApplicationPreferenceById(id);
        try {
            return applicationPreferenceRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationPreference not found with id: " + id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationPreferenceById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_preference_by_id_svc_yy0", hash = "dd87a2da", zone = 2)
    public void beforeGetApplicationPreferenceById(String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_preference_by_id_svc_yy0", hash = "87678a7f", zone = 2)
    public void afterGetApplicationPreferenceById(String id) {
    }

    @MethodMetadata(irId = "list_application_preferences_svc_0c0", hash = "de84bc83", zone = 1)
    public List<ApplicationPreference> listApplicationPreferences() {
        beforeListApplicationPreferences();
        try {
            return applicationPreferenceRepository.findAll();
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationPreferences();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_preferences_svc_0c0", hash = "4a29a456", zone = 2)
    public void beforeListApplicationPreferences() {
    }

    @MethodMetadata(irId = "hook-after-list_application_preferences_svc_0c0", hash = "b4f0317d", zone = 2)
    public void afterListApplicationPreferences() {
    }

    @MethodMetadata(irId = "update_application_preference_svc_tmm", hash = "e68c7d60", zone = 1)
    public ApplicationPreference updateApplicationPreference(String id, UpdateApplicationPreferenceRequest request) {
        beforeUpdateApplicationPreference(id, request);
        try {
            ApplicationPreference existing = applicationPreferenceRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationPreference not found with id: " + id));
            if (request.getApplicationId() != null) {
                Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
                existing.setApplication(application);
            }
            if (request.getPreferenceOrder() != null) {
                existing.setPreferenceOrder(request.getPreferenceOrder());
            }
            if (request.getProgramme() != null) {
                existing.setProgramme(request.getProgramme());
            }
            return applicationPreferenceRepository.save(existing);
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateApplicationPreference(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_application_preference_svc_tmm", hash = "a9731456", zone = 2)
    public void beforeUpdateApplicationPreference(String id, UpdateApplicationPreferenceRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_application_preference_svc_tmm", hash = "868793e0", zone = 2)
    public void afterUpdateApplicationPreference(String id, UpdateApplicationPreferenceRequest request) {
    }

    @MethodMetadata(irId = "delete_application_preference_svc_nfy", hash = "526e02ed", zone = 1)
    public void deleteApplicationPreference(String id) {
        beforeDeleteApplicationPreference(id);
        try {
            applicationPreferenceRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationPreference not found with id: " + id));
            applicationPreferenceRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationPreference(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_preference_svc_nfy", hash = "cfda5cb4", zone = 2)
    public void beforeDeleteApplicationPreference(String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_preference_svc_nfy", hash = "bb7a385f", zone = 2)
    public void afterDeleteApplicationPreference(String id) {
    }
}
