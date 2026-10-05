package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateQueryMessageRequest;
import com.example.app.dto.UpdateQueryMessageRequest;
import com.example.app.entity.QueryMessage;
import com.example.app.service.QueryMessageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/query-messages")
public class QueryMessageController {

    @Autowired()
    private QueryMessageService queryMessageService;

    @MethodMetadata(irId = "create_query_message_ctrl_11t", hash = "2ac2cfa1", zone = 1)
    @PostMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<QueryMessage> createQueryMessage(@RequestParam(required = false) CreateQueryMessageRequest request) {
        beforeCreateQueryMessage(request);
        try {
            return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(queryMessageService.createQueryMessage(request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateQueryMessage(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_query_message_ctrl_11t", hash = "fd54f991", zone = 2)
    public void beforeCreateQueryMessage(@RequestParam(required = false) CreateQueryMessageRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_query_message_ctrl_11t", hash = "5c02930b", zone = 2)
    public void afterCreateQueryMessage(@RequestParam(required = false) CreateQueryMessageRequest request) {
    }

    @MethodMetadata(irId = "get_query_message_by_id_ctrl_qy1", hash = "899a3d28", zone = 1)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<QueryMessage> getQueryMessageById(@PathVariable String id) {
        beforeGetQueryMessageById(id);
        try {
            return ResponseEntity.ok(queryMessageService.getQueryMessageById(id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetQueryMessageById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_query_message_by_id_ctrl_qy1", hash = "8064828b", zone = 2)
    public void beforeGetQueryMessageById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-get_query_message_by_id_ctrl_qy1", hash = "562f9e2a", zone = 2)
    public void afterGetQueryMessageById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "list_query_messages_ctrl_dt4", hash = "42200c21", zone = 1)
    @GetMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<List<QueryMessage>> listQueryMessages() {
        beforeListQueryMessages();
        try {
            return ResponseEntity.ok(queryMessageService.listQueryMessages());
        } catch (Exception e) {
            throw e;
        } finally {
            afterListQueryMessages();
        }
    }

    @MethodMetadata(irId = "hook-before-list_query_messages_ctrl_dt4", hash = "114667e6", zone = 2)
    public void beforeListQueryMessages() {
    }

    @MethodMetadata(irId = "hook-after-list_query_messages_ctrl_dt4", hash = "ac9a737e", zone = 2)
    public void afterListQueryMessages() {
    }

    @MethodMetadata(irId = "update_query_message_ctrl_nds", hash = "105d3833", zone = 1)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<QueryMessage> updateQueryMessage(@PathVariable String id, @RequestParam(required = false) UpdateQueryMessageRequest request) {
        beforeUpdateQueryMessage(id, request);
        try {
            return ResponseEntity.ok(queryMessageService.updateQueryMessage(id, request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateQueryMessage(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_query_message_ctrl_nds", hash = "e354d3f7", zone = 2)
    public void beforeUpdateQueryMessage(@PathVariable String id, @RequestParam(required = false) UpdateQueryMessageRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_query_message_ctrl_nds", hash = "19886b9e", zone = 2)
    public void afterUpdateQueryMessage(@PathVariable String id, @RequestParam(required = false) UpdateQueryMessageRequest request) {
    }

    @MethodMetadata(irId = "delete_query_message_ctrl_yvq", hash = "0d3dbc2a", zone = 1)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Void> deleteQueryMessage(@PathVariable String id) {
        beforeDeleteQueryMessage(id);
        try {
            queryMessageService.deleteQueryMessage(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteQueryMessage(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_query_message_ctrl_yvq", hash = "a35a1838", zone = 2)
    public void beforeDeleteQueryMessage(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_query_message_ctrl_yvq", hash = "662b6153", zone = 2)
    public void afterDeleteQueryMessage(@PathVariable String id) {
    }
}
