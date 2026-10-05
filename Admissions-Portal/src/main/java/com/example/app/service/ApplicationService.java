package com.example.app.service;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.*;
import com.example.app.entity.*;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.ApplicationDocumentRepository;
import com.example.app.repository.ApplicationEducationRepository;
import com.example.app.repository.ApplicationExamScoreRepository;
import com.example.app.repository.ApplicationPaymentRepository;
import com.example.app.repository.ApplicationPreferenceRepository;
import com.example.app.repository.ApplicationRepository;
import com.example.app.repository.DriveRepository;
import com.example.app.repository.UserRepository;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service()
public class ApplicationService {

    @MethodMetadata(irId = "get_application_by_id_svc_y6l", hash = "b496bcac", zone = 1)
    public Application getApplicationById(String id) {
        beforeGetApplicationById(id);
        try {
            return applicationRepository.findById(id).orElseThrow(() -> new NotFoundException("Application not found with id: " + id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_by_id_svc_y6l", hash = "dfc99b83", zone = 2)
    public void beforeGetApplicationById(String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_by_id_svc_y6l", hash = "3dad53ce", zone = 2)
    public void afterGetApplicationById(String id) {
    }

    @MethodMetadata(irId = "list_applications_svc_g14", hash = "9aef39b3", zone = 1)
    public List<Application> listApplications() {
        beforeListApplications();
        try {
            return applicationRepository.findAll();
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplications();
        }
    }

    @MethodMetadata(irId = "hook-before-list_applications_svc_g14", hash = "35e05818", zone = 2)
    public void beforeListApplications() {
    }

    @MethodMetadata(irId = "hook-after-list_applications_svc_g14", hash = "d5db7e81", zone = 2)
    public void afterListApplications() {
    }

    @MethodMetadata(irId = "delete_application_svc_8i2", hash = "78ab0b2d", zone = 1)
    public void deleteApplication(String id) {
        beforeDeleteApplication(id);
        try {
            applicationRepository.findById(id).orElseThrow(() -> new NotFoundException("Application not found with id: " + id));
            applicationRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplication(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_svc_8i2", hash = "836c9686", zone = 2)
    public void beforeDeleteApplication(String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_svc_8i2", hash = "f5c42558", zone = 2)
    public void afterDeleteApplication(String id) {
    }

    @Autowired()
    private ApplicationRepository applicationRepository;

    @Autowired()
    private DriveRepository driveRepository;

    @Autowired()
    private UserRepository userRepository;

    @Autowired()
    private KeycloakAuthService keycloakAuthService;

    /*
 * Operation    : Start New Application for Drive
 * Usecase ID   : UC-01
 * Usecase Name : Start New Application for Active Drive
 */
    @MethodMetadata(irId = "startNewApplicationForDrive_gdm", hash = "e390760d", zone = 1)
    public String startNewApplicationForDrive(StartApplicationRequest request, Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Application existingApp = applicationRepository.findByUserIdAndDriveId(userId, request.getDriveId()).orElse(null);
        if (existingApp != null) {
            throw new RuntimeException("Application already exists for this drive");
        }
        Drive drive = driveRepository.findById(request.getDriveId()).orElseThrow(() -> new RuntimeException("Drive not found"));
        String prefix = "APBTIMT".equals(drive.getDriveType()) ? "APBTIMT" : "APMT";
        String applicationNumber = prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Application app = new Application();
        app.setId(UUID.randomUUID().toString());
        app.setUserId(userId);
        app.setDriveId(request.getDriveId());
        app.setApplicationNumber(applicationNumber);
        // fallback; real value should come from request or user profile
        app.setFullName(user.getEmail());
        app.setEmail(user.getEmail());
        app.setStatus("DRAFT");
        app.setJeeMainVerificationStatus("PENDING");
        app.setJeeAdvancedVerificationStatus("PENDING");
        applicationRepository.save(app);
        return applicationNumber;
    }

    @Autowired()
    private ApplicationEducationRepository applicationEducationRepository;

    @Autowired()
    private ApplicationExamScoreRepository applicationExamScoreRepository;

    @Autowired()
    private ApplicationPreferenceRepository applicationPreferenceRepository;

    @Autowired()
    private ApplicationDocumentRepository applicationDocumentRepository;

    /*
 * Operation    : Save Draft Application
 * Usecase ID   : UC-02
 * Usecase Name : Save Draft Application
 */
    @MethodMetadata(irId = "saveDraftApplication_jjb", hash = "de998bf7", zone = 1)
    public String saveDraftApplication(SaveDraftApplicationRequest request, Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        Application application = request.getApplication();
        if (application.getId() != null) {
            Application existing = applicationRepository.findById(application.getId()).orElseThrow(() -> new RuntimeException("Application not found"));
            if (!userId.equals(existing.getUserId())) {
                throw new RuntimeException("Unauthorized: draft belongs to another user");
            }
        } else {
            application.setId(java.util.UUID.randomUUID().toString());
        }
        application.setUserId(userId);
        application.setStatus("DRAFT");
        Application saved = applicationRepository.save(application);
        List<ApplicationEducation> educations = request.getEducations();
        if (educations != null) {
            for (ApplicationEducation edu : educations) {
                edu.setId(null);
                edu.setApplicationId(saved.getId());
            }
            applicationEducationRepository.saveAll(educations);
        }
        List<ApplicationExamScore> examScores = request.getExamScores();
        if (examScores != null) {
            for (ApplicationExamScore score : examScores) {
                score.setId(null);
                score.setApplicationId(saved.getId());
            }
            applicationExamScoreRepository.saveAll(examScores);
        }
        List<ApplicationPreference> preferences = request.getPreferences();
        if (preferences != null) {
            for (ApplicationPreference pref : preferences) {
                pref.setId(null);
                pref.setApplicationId(saved.getId());
            }
            applicationPreferenceRepository.saveAll(preferences);
        }
        List<ApplicationDocument> documents = request.getDocuments();
        if (documents != null) {
            for (ApplicationDocument doc : documents) {
                doc.setId(null);
                doc.setApplicationId(saved.getId());
            }
            applicationDocumentRepository.saveAll(documents);
        }
        return saved.getId();
    }

    @Autowired()
    private ApplicationPaymentRepository applicationPaymentRepository;

    /*
 * Operation    : Submit Application After Payment
 * Usecase ID   : UC-03
 * Usecase Name : Submit Application After Payment
 */
    @MethodMetadata(irId = "submitApplicationAfterPayment_uf5", hash = "9230a84a", zone = 1)
    public Void submitApplicationAfterPayment(String applicationId, Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        // Verify ownership: application must belong to the authenticated user
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("Application not found"));
        if (!userId.equals(application.getUserId())) {
            throw new RuntimeException("Unauthorized access to application");
        }
        // Verify associated payment is completed successfully
        List<ApplicationPayment> payments = applicationPaymentRepository.findByApplicationIdAndPaymentStatus(applicationId, "SUCCESS");
        if (payments == null || payments.isEmpty()) {
            throw new RuntimeException("No successful payment found for this application");
        }
        // Update status to SUBMITTED and lock the application
        application.setStatus("SUBMITTED");
        applicationRepository.save(application);
        return null;
    }

    /*
 * Operation    : Upload and Link Document to Application
 * Usecase ID   : UC-04
 * Usecase Name : Upload Supporting Documents
 */
    @MethodMetadata(irId = "uploadAndLinkDocumentToApplication_kh5", hash = "e044ff4d", zone = 1)
    public String uploadAndLinkDocumentToApplication(UploadDocumentRequest request, Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new RuntimeException("Application not found"));
        if (!userId.equals(application.getUserId())) {
            throw new RuntimeException("User does not own this application");
        }
        String status = application.getStatus();
        if (!"DRAFT".equals(status) && !"EDITING".equals(status)) {
            throw new RuntimeException("Application is not in an editable state (must be DRAFT or EDITING)");
        }
        String fileReference = request.getFileReference();
        if (fileReference == null || fileReference.isEmpty()) {
            throw new RuntimeException("File reference from DMS is required");
        }
        ApplicationDocument applicationDocument = new ApplicationDocument();
        applicationDocument.setId(java.util.UUID.randomUUID().toString());
        applicationDocument.setApplicationId(request.getApplicationId());
        applicationDocument.setDocumentType(request.getDocumentType());
        applicationDocument.setFileReference(fileReference);
        applicationDocument.setUploadedBy(userId);
        applicationDocument.setRoundId(request.getRoundId());
        applicationDocumentRepository.save(applicationDocument);
        return applicationDocument.getId();
    }
}
