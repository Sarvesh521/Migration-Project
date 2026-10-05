package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.*;
import com.example.app.entity.*;
import com.example.app.service.QueryService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/queries")
public class QueryController {

    @Autowired()
    private QueryService queryService;

    @MethodMetadata(irId = "get_query_by_id_ctrl_rah", hash = "5af5bcf6", zone = 1)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Query> getQueryById(@PathVariable String id) {
        beforeGetQueryById(id);
        try {
            return ResponseEntity.ok(queryService.getQueryById(id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetQueryById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_query_by_id_ctrl_rah", hash = "58ddf18a", zone = 2)
    public void beforeGetQueryById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-get_query_by_id_ctrl_rah", hash = "5391758a", zone = 2)
    public void afterGetQueryById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "list_querys_ctrl_jge", hash = "b2092607", zone = 1)
    @GetMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<List<Query>> listQuerys() {
        beforeListQuerys();
        try {
            return ResponseEntity.ok(queryService.listQuerys());
        } catch (Exception e) {
            throw e;
        } finally {
            afterListQuerys();
        }
    }

    @MethodMetadata(irId = "hook-before-list_querys_ctrl_jge", hash = "143fb373", zone = 2)
    public void beforeListQuerys() {
    }

    @MethodMetadata(irId = "hook-after-list_querys_ctrl_jge", hash = "be5e076e", zone = 2)
    public void afterListQuerys() {
    }

    @MethodMetadata(irId = "update_query_ctrl_uo8", hash = "bbd9768f", zone = 1)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Query> updateQuery(@PathVariable String id, @RequestParam(required = false) UpdateQueryRequest request) {
        beforeUpdateQuery(id, request);
        try {
            return ResponseEntity.ok(queryService.updateQuery(id, request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateQuery(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_query_ctrl_uo8", hash = "29f53898", zone = 2)
    public void beforeUpdateQuery(@PathVariable String id, @RequestParam(required = false) UpdateQueryRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_query_ctrl_uo8", hash = "a2d15977", zone = 2)
    public void afterUpdateQuery(@PathVariable String id, @RequestParam(required = false) UpdateQueryRequest request) {
    }

    @MethodMetadata(irId = "delete_query_ctrl_2rt", hash = "ff695a66", zone = 1)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Void> deleteQuery(@PathVariable String id) {
        beforeDeleteQuery(id);
        try {
            queryService.deleteQuery(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteQuery(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_query_ctrl_2rt", hash = "a5c43203", zone = 2)
    public void beforeDeleteQuery(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_query_ctrl_2rt", hash = "f69cf6db", zone = 2)
    public void afterDeleteQuery(@PathVariable String id) {
    }

    /*
 * Operation    : Respond to Query and Update Status
 * Usecase ID   : UC-19
 * Usecase Name : Respond to Query / Update Status
 */
    @MethodMetadata(irId = "respondToQueryAndUpdateStatus_uxw", hash = "67097e7e", zone = 1)
    @PostMapping(value = "/{queryId}/respond")
    public ResponseEntity<Void> respondToQueryAndUpdateStatus(@PathVariable String queryId, @RequestBody @Valid RespondToQueryRequest request, Principal principal) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(queryService.respondToQueryAndUpdateStatus(queryId, request.getReplyText(), request.getNewStatus(), principal));
    }
}
