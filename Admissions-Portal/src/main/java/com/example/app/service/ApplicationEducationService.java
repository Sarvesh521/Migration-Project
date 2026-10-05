package com.example.app.service;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateApplicationEducationRequest;
import com.example.app.dto.UpdateApplicationEducationRequest;
import com.example.app.entity.ApplicationEducation;
import com.example.app.exception.NotFoundException;
import com.example.app.entity.Application;
import com.example.app.repository.ApplicationRepository;
import com.example.app.repository.ApplicationEducationRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service()
public class ApplicationEducationService {

    @Autowired()
    private ApplicationRepository applicationRepository;

    @Autowired()
    private ApplicationEducationRepository applicationEducationRepository;

    @MethodMetadata(irId = "create_application_education_svc_hpo", hash = "3e546c36", zone = 1)
    public ApplicationEducation createApplicationEducation(CreateApplicationEducationRequest request) {
        beforeCreateApplicationEducation(request);
        try {
            Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
            ApplicationEducation applicationEducation = new ApplicationEducation();
            applicationEducation.setEducationLevel(request.getEducationLevel());
            applicationEducation.setBoardUniversity(request.getBoardUniversity());
            applicationEducation.setInstitutionName(request.getInstitutionName());
            applicationEducation.setSpecialization(request.getSpecialization());
            applicationEducation.setApplication(application);
            return applicationEducationRepository.save(applicationEducation);
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationEducation(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_education_svc_hpo", hash = "94af23da", zone = 2)
    public void beforeCreateApplicationEducation(CreateApplicationEducationRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_education_svc_hpo", hash = "8bdfa898", zone = 2)
    public void afterCreateApplicationEducation(CreateApplicationEducationRequest request) {
    }

    @MethodMetadata(irId = "get_application_education_by_id_svc_4v3", hash = "b56d70b4", zone = 1)
    public ApplicationEducation getApplicationEducationById(String id) {
        beforeGetApplicationEducationById(id);
        try {
            return applicationEducationRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationEducation not found with id: " + id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationEducationById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_education_by_id_svc_4v3", hash = "45cec1d6", zone = 2)
    public void beforeGetApplicationEducationById(String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_education_by_id_svc_4v3", hash = "9b6b6f3b", zone = 2)
    public void afterGetApplicationEducationById(String id) {
    }

    @MethodMetadata(irId = "list_application_educations_svc_f7b", hash = "1a04f1f3", zone = 1)
    public List<ApplicationEducation> listApplicationEducations() {
        beforeListApplicationEducations();
        try {
            return applicationEducationRepository.findAll();
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationEducations();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_educations_svc_f7b", hash = "e00102a1", zone = 2)
    public void beforeListApplicationEducations() {
    }

    @MethodMetadata(irId = "hook-after-list_application_educations_svc_f7b", hash = "afef07db", zone = 2)
    public void afterListApplicationEducations() {
    }

    @MethodMetadata(irId = "update_application_education_svc_b0l", hash = "d5aabf9c", zone = 1)
    public ApplicationEducation updateApplicationEducation(String id, UpdateApplicationEducationRequest request) {
        beforeUpdateApplicationEducation(id, request);
        try {
            ApplicationEducation existing = applicationEducationRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationEducation not found with id: " + id));
            if (request.getApplicationId() != null) {
                Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
                existing.setApplication(application);
            }
            if (request.getEducationLevel() != null) {
                existing.setEducationLevel(request.getEducationLevel());
            }
            if (request.getBoardUniversity() != null) {
                existing.setBoardUniversity(request.getBoardUniversity());
            }
            if (request.getInstitutionName() != null) {
                existing.setInstitutionName(request.getInstitutionName());
            }
            if (request.getSpecialization() != null) {
                existing.setSpecialization(request.getSpecialization());
            }
            return applicationEducationRepository.save(existing);
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateApplicationEducation(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_application_education_svc_b0l", hash = "84992e36", zone = 2)
    public void beforeUpdateApplicationEducation(String id, UpdateApplicationEducationRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_application_education_svc_b0l", hash = "a0854110", zone = 2)
    public void afterUpdateApplicationEducation(String id, UpdateApplicationEducationRequest request) {
    }

    @MethodMetadata(irId = "delete_application_education_svc_hy1", hash = "78a31ebf", zone = 1)
    public void deleteApplicationEducation(String id) {
        beforeDeleteApplicationEducation(id);
        try {
            applicationEducationRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationEducation not found with id: " + id));
            applicationEducationRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationEducation(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_education_svc_hy1", hash = "75133ade", zone = 2)
    public void beforeDeleteApplicationEducation(String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_education_svc_hy1", hash = "b25d0f4d", zone = 2)
    public void afterDeleteApplicationEducation(String id) {
    }
}
