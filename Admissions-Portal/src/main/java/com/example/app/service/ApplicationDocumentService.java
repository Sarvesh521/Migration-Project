package com.example.app.service;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateApplicationDocumentRequest;
import com.example.app.dto.UpdateApplicationDocumentRequest;
import com.example.app.entity.ApplicationDocument;
import com.example.app.entity.Application;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.ApplicationRepository;
import com.example.app.repository.ApplicationDocumentRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service()
public class ApplicationDocumentService {

    @Autowired()
    private ApplicationRepository applicationRepository;

    @Autowired()
    private ApplicationDocumentRepository applicationDocumentRepository;

    @MethodMetadata(irId = "create_application_document_svc_6qw", hash = "4cb21d4f", zone = 1)
    public ApplicationDocument createApplicationDocument(CreateApplicationDocumentRequest request) {
        beforeCreateApplicationDocument(request);
        try {
            Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
            ApplicationDocument applicationDocument = new ApplicationDocument();
            applicationDocument.setDocumentType(request.getDocumentType());
            applicationDocument.setFileReference(request.getFileReference());
            applicationDocument.setRoundId(request.getRoundId());
            applicationDocument.setUploadedBy(request.getUploadedBy());
            applicationDocument.setApplication(application);
            return applicationDocumentRepository.save(applicationDocument);
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationDocument(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_document_svc_6qw", hash = "fc8a6850", zone = 2)
    public void beforeCreateApplicationDocument(CreateApplicationDocumentRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_document_svc_6qw", hash = "d253ff30", zone = 2)
    public void afterCreateApplicationDocument(CreateApplicationDocumentRequest request) {
    }

    @MethodMetadata(irId = "get_application_document_by_id_svc_rwo", hash = "863c9100", zone = 1)
    public ApplicationDocument getApplicationDocumentById(String id) {
        beforeGetApplicationDocumentById(id);
        try {
            return applicationDocumentRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationDocument not found with id: " + id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationDocumentById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_document_by_id_svc_rwo", hash = "82e9b28b", zone = 2)
    public void beforeGetApplicationDocumentById(String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_document_by_id_svc_rwo", hash = "1561a574", zone = 2)
    public void afterGetApplicationDocumentById(String id) {
    }

    @MethodMetadata(irId = "list_application_documents_svc_nck", hash = "6c561735", zone = 1)
    public List<ApplicationDocument> listApplicationDocuments() {
        beforeListApplicationDocuments();
        try {
            return applicationDocumentRepository.findAll();
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationDocuments();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_documents_svc_nck", hash = "ad8ef0b6", zone = 2)
    public void beforeListApplicationDocuments() {
    }

    @MethodMetadata(irId = "hook-after-list_application_documents_svc_nck", hash = "8ff4537c", zone = 2)
    public void afterListApplicationDocuments() {
    }

    @MethodMetadata(irId = "update_application_document_svc_8fj", hash = "9ae6bc90", zone = 1)
    public ApplicationDocument updateApplicationDocument(String id, UpdateApplicationDocumentRequest request) {
        beforeUpdateApplicationDocument(id, request);
        try {
            ApplicationDocument existing = applicationDocumentRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationDocument not found with id: " + id));
            if (request.getApplicationId() != null) {
                Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
                existing.setApplication(application);
            }
            if (request.getDocumentType() != null) {
                existing.setDocumentType(request.getDocumentType());
            }
            if (request.getFileReference() != null) {
                existing.setFileReference(request.getFileReference());
            }
            if (request.getRoundId() != null) {
                existing.setRoundId(request.getRoundId());
            }
            if (request.getUploadedBy() != null) {
                existing.setUploadedBy(request.getUploadedBy());
            }
            return applicationDocumentRepository.save(existing);
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateApplicationDocument(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_application_document_svc_8fj", hash = "cf559002", zone = 2)
    public void beforeUpdateApplicationDocument(String id, UpdateApplicationDocumentRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_application_document_svc_8fj", hash = "3478f135", zone = 2)
    public void afterUpdateApplicationDocument(String id, UpdateApplicationDocumentRequest request) {
    }

    @MethodMetadata(irId = "delete_application_document_svc_122", hash = "08898e72", zone = 1)
    public void deleteApplicationDocument(String id) {
        beforeDeleteApplicationDocument(id);
        try {
            applicationDocumentRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationDocument not found with id: " + id));
            applicationDocumentRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationDocument(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_document_svc_122", hash = "2aa7de57", zone = 2)
    public void beforeDeleteApplicationDocument(String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_document_svc_122", hash = "9bcd5164", zone = 2)
    public void afterDeleteApplicationDocument(String id) {
    }
}
