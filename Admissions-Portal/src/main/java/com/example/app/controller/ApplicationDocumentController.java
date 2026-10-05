package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateApplicationDocumentRequest;
import com.example.app.dto.UpdateApplicationDocumentRequest;
import com.example.app.entity.ApplicationDocument;
import com.example.app.service.ApplicationDocumentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/application-documents")
public class ApplicationDocumentController {

    @Autowired()
    private ApplicationDocumentService applicationDocumentService;

    @MethodMetadata(irId = "create_application_document_ctrl_13h", hash = "d6a2f05d", zone = 1)
    @PostMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationDocument> createApplicationDocument(@RequestParam(required = false) CreateApplicationDocumentRequest request) {
        beforeCreateApplicationDocument(request);
        try {
            return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(applicationDocumentService.createApplicationDocument(request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationDocument(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_document_ctrl_13h", hash = "ebdf6cfa", zone = 2)
    public void beforeCreateApplicationDocument(@RequestParam(required = false) CreateApplicationDocumentRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_document_ctrl_13h", hash = "05676bb0", zone = 2)
    public void afterCreateApplicationDocument(@RequestParam(required = false) CreateApplicationDocumentRequest request) {
    }

    @MethodMetadata(irId = "get_application_document_by_id_ctrl_pby", hash = "06f59904", zone = 1)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationDocument> getApplicationDocumentById(@PathVariable String id) {
        beforeGetApplicationDocumentById(id);
        try {
            return ResponseEntity.ok(applicationDocumentService.getApplicationDocumentById(id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationDocumentById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_document_by_id_ctrl_pby", hash = "74865dfd", zone = 2)
    public void beforeGetApplicationDocumentById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_document_by_id_ctrl_pby", hash = "6b8e68fc", zone = 2)
    public void afterGetApplicationDocumentById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "list_application_documents_ctrl_n6r", hash = "77d24fe3", zone = 1)
    @GetMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<List<ApplicationDocument>> listApplicationDocuments() {
        beforeListApplicationDocuments();
        try {
            return ResponseEntity.ok(applicationDocumentService.listApplicationDocuments());
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationDocuments();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_documents_ctrl_n6r", hash = "ad8ef0b6", zone = 2)
    public void beforeListApplicationDocuments() {
    }

    @MethodMetadata(irId = "hook-after-list_application_documents_ctrl_n6r", hash = "8ff4537c", zone = 2)
    public void afterListApplicationDocuments() {
    }

    @MethodMetadata(irId = "update_application_document_ctrl_jwg", hash = "e2d10f29", zone = 1)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<ApplicationDocument> updateApplicationDocument(@PathVariable String id, @RequestParam(required = false) UpdateApplicationDocumentRequest request) {
        beforeUpdateApplicationDocument(id, request);
        try {
            return ResponseEntity.ok(applicationDocumentService.updateApplicationDocument(id, request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateApplicationDocument(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_application_document_ctrl_jwg", hash = "0a36f937", zone = 2)
    public void beforeUpdateApplicationDocument(@PathVariable String id, @RequestParam(required = false) UpdateApplicationDocumentRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_application_document_ctrl_jwg", hash = "f8d94366", zone = 2)
    public void afterUpdateApplicationDocument(@PathVariable String id, @RequestParam(required = false) UpdateApplicationDocumentRequest request) {
    }

    @MethodMetadata(irId = "delete_application_document_ctrl_y2b", hash = "7a6c8297", zone = 1)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Void> deleteApplicationDocument(@PathVariable String id) {
        beforeDeleteApplicationDocument(id);
        try {
            applicationDocumentService.deleteApplicationDocument(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationDocument(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_document_ctrl_y2b", hash = "0fc31331", zone = 2)
    public void beforeDeleteApplicationDocument(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_document_ctrl_y2b", hash = "8202218e", zone = 2)
    public void afterDeleteApplicationDocument(@PathVariable String id) {
    }
}
