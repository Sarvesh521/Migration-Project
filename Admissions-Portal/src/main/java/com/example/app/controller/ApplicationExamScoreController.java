package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateApplicationExamScoreRequest;
import com.example.app.dto.UpdateApplicationExamScoreRequest;
import com.example.app.entity.ApplicationExamScore;
import com.example.app.service.ApplicationExamScoreService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/application-exam-scores")
public class ApplicationExamScoreController {

    @Autowired()
    private ApplicationExamScoreService applicationExamScoreService;

    @MethodMetadata(irId = "create_application_exam_score_ctrl_waa", hash = "1e6a3f02", zone = 1)
    @PostMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationExamScore> createApplicationExamScore(@RequestParam(required = false) CreateApplicationExamScoreRequest request) {
        beforeCreateApplicationExamScore(request);
        try {
            return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationExamScoreService.createApplicationExamScore(request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationExamScore(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_exam_score_ctrl_waa", hash = "e0614a80", zone = 2)
    public void beforeCreateApplicationExamScore(@RequestParam(required = false) CreateApplicationExamScoreRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_exam_score_ctrl_waa", hash = "9bd6858a", zone = 2)
    public void afterCreateApplicationExamScore(@RequestParam(required = false) CreateApplicationExamScoreRequest request) {
    }

    @MethodMetadata(irId = "get_application_exam_score_by_id_ctrl_rr5", hash = "50b6796f", zone = 1)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationExamScore> getApplicationExamScoreById(@PathVariable String id) {
        beforeGetApplicationExamScoreById(id);
        try {
            return ResponseEntity.ok(applicationExamScoreService.getApplicationExamScoreById(id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationExamScoreById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_exam_score_by_id_ctrl_rr5", hash = "2f24faab", zone = 2)
    public void beforeGetApplicationExamScoreById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_exam_score_by_id_ctrl_rr5", hash = "4c330428", zone = 2)
    public void afterGetApplicationExamScoreById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "list_application_exam_scores_ctrl_8h4", hash = "a08175a7", zone = 1)
    @GetMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<List<ApplicationExamScore>> listApplicationExamScores() {
        beforeListApplicationExamScores();
        try {
            return ResponseEntity.ok(applicationExamScoreService.listApplicationExamScores());
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationExamScores();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_exam_scores_ctrl_8h4", hash = "8a1fa589", zone = 2)
    public void beforeListApplicationExamScores() {
    }

    @MethodMetadata(irId = "hook-after-list_application_exam_scores_ctrl_8h4", hash = "23bd68dd", zone = 2)
    public void afterListApplicationExamScores() {
    }

    @MethodMetadata(irId = "update_application_exam_score_ctrl_ths", hash = "b48d87c4", zone = 1)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationExamScore> updateApplicationExamScore(@PathVariable String id, @RequestParam(required = false) UpdateApplicationExamScoreRequest request) {
        beforeUpdateApplicationExamScore(id, request);
        try {
            return ResponseEntity.ok(applicationExamScoreService.updateApplicationExamScore(id, request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateApplicationExamScore(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_application_exam_score_ctrl_ths", hash = "1270b9ea", zone = 2)
    public void beforeUpdateApplicationExamScore(@PathVariable String id, @RequestParam(required = false) UpdateApplicationExamScoreRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_application_exam_score_ctrl_ths", hash = "466bd39e", zone = 2)
    public void afterUpdateApplicationExamScore(@PathVariable String id, @RequestParam(required = false) UpdateApplicationExamScoreRequest request) {
    }

    @MethodMetadata(irId = "delete_application_exam_score_ctrl_5et", hash = "2161f83c", zone = 1)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Void> deleteApplicationExamScore(@PathVariable String id) {
        beforeDeleteApplicationExamScore(id);
        try {
            applicationExamScoreService.deleteApplicationExamScore(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationExamScore(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_exam_score_ctrl_5et", hash = "caff908d", zone = 2)
    public void beforeDeleteApplicationExamScore(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_exam_score_ctrl_5et", hash = "c0312185", zone = 2)
    public void afterDeleteApplicationExamScore(@PathVariable String id) {
    }
}
