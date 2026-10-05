package com.example.app.service;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateApplicationExamScoreRequest;
import com.example.app.dto.UpdateApplicationExamScoreRequest;
import com.example.app.entity.ApplicationExamScore;
import com.example.app.entity.Application;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.ApplicationRepository;
import com.example.app.repository.ApplicationExamScoreRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service()
public class ApplicationExamScoreService {

    @Autowired()
    private ApplicationRepository applicationRepository;

    @Autowired()
    private ApplicationExamScoreRepository applicationExamScoreRepository;

    @MethodMetadata(irId = "create_application_exam_score_svc_shp", hash = "e4999561", zone = 1)
    public ApplicationExamScore createApplicationExamScore(CreateApplicationExamScoreRequest request) {
        beforeCreateApplicationExamScore(request);
        try {
            Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
            ApplicationExamScore applicationExamScore = new ApplicationExamScore();
            applicationExamScore.setExamType(request.getExamType());
            applicationExamScore.setRollNumber(request.getRollNumber());
            applicationExamScore.setScore(request.getScore());
            applicationExamScore.setRank(request.getRank());
            applicationExamScore.setExamYear(request.getExamYear());
            applicationExamScore.setSubjects(request.getSubjects());
            applicationExamScore.setApplication(application);
            return applicationExamScoreRepository.save(applicationExamScore);
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationExamScore(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_exam_score_svc_shp", hash = "25db8500", zone = 2)
    public void beforeCreateApplicationExamScore(CreateApplicationExamScoreRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_exam_score_svc_shp", hash = "fc5e3dac", zone = 2)
    public void afterCreateApplicationExamScore(CreateApplicationExamScoreRequest request) {
    }

    @MethodMetadata(irId = "get_application_exam_score_by_id_svc_sfs", hash = "f16b81ba", zone = 1)
    public ApplicationExamScore getApplicationExamScoreById(String id) {
        beforeGetApplicationExamScoreById(id);
        try {
            return applicationExamScoreRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationExamScore not found with id: " + id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationExamScoreById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_exam_score_by_id_svc_sfs", hash = "87dc1d0f", zone = 2)
    public void beforeGetApplicationExamScoreById(String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_exam_score_by_id_svc_sfs", hash = "846987b9", zone = 2)
    public void afterGetApplicationExamScoreById(String id) {
    }

    @MethodMetadata(irId = "list_application_exam_scores_svc_qvk", hash = "61d15de7", zone = 1)
    public List<ApplicationExamScore> listApplicationExamScores() {
        beforeListApplicationExamScores();
        try {
            return applicationExamScoreRepository.findAll();
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationExamScores();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_exam_scores_svc_qvk", hash = "8a1fa589", zone = 2)
    public void beforeListApplicationExamScores() {
    }

    @MethodMetadata(irId = "hook-after-list_application_exam_scores_svc_qvk", hash = "23bd68dd", zone = 2)
    public void afterListApplicationExamScores() {
    }

    @MethodMetadata(irId = "update_application_exam_score_svc_ncv", hash = "f9c66dbc", zone = 1)
    public ApplicationExamScore updateApplicationExamScore(String id, UpdateApplicationExamScoreRequest request) {
        beforeUpdateApplicationExamScore(id, request);
        try {
            ApplicationExamScore existing = applicationExamScoreRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationExamScore not found with id: " + id));
            if (request.getApplicationId() != null) {
                Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
                existing.setApplication(application);
            }
            if (request.getExamType() != null) {
                existing.setExamType(request.getExamType());
            }
            if (request.getRollNumber() != null) {
                existing.setRollNumber(request.getRollNumber());
            }
            if (request.getScore() != null) {
                existing.setScore(request.getScore());
            }
            if (request.getRank() != null) {
                existing.setRank(request.getRank());
            }
            if (request.getExamYear() != null) {
                existing.setExamYear(request.getExamYear());
            }
            if (request.getSubjects() != null) {
                existing.setSubjects(request.getSubjects());
            }
            return applicationExamScoreRepository.save(existing);
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateApplicationExamScore(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_application_exam_score_svc_ncv", hash = "d688dffd", zone = 2)
    public void beforeUpdateApplicationExamScore(String id, UpdateApplicationExamScoreRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_application_exam_score_svc_ncv", hash = "b63b34cd", zone = 2)
    public void afterUpdateApplicationExamScore(String id, UpdateApplicationExamScoreRequest request) {
    }

    @MethodMetadata(irId = "delete_application_exam_score_svc_3gj", hash = "3cdf7073", zone = 1)
    public void deleteApplicationExamScore(String id) {
        beforeDeleteApplicationExamScore(id);
        try {
            applicationExamScoreRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationExamScore not found with id: " + id));
            applicationExamScoreRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationExamScore(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_exam_score_svc_3gj", hash = "b40bbacb", zone = 2)
    public void beforeDeleteApplicationExamScore(String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_exam_score_svc_3gj", hash = "9337496d", zone = 2)
    public void afterDeleteApplicationExamScore(String id) {
    }
}
