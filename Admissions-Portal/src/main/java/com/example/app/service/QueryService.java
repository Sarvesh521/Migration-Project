package com.example.app.service;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.*;
import com.example.app.entity.*;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.ApplicationRepository;
import com.example.app.repository.QueryMessageRepository;
import com.example.app.repository.QueryRepository;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service()
public class QueryService {

    @Autowired()
    private ApplicationRepository applicationRepository;

    @MethodMetadata(irId = "get_query_by_id_svc_iwa", hash = "0137d09c", zone = 1)
    public Query getQueryById(String id) {
        beforeGetQueryById(id);
        try {
            return queryRepository.findById(id).orElseThrow(() -> new NotFoundException("Query not found with id: " + id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetQueryById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_query_by_id_svc_iwa", hash = "31927ef7", zone = 2)
    public void beforeGetQueryById(String id) {
    }

    @MethodMetadata(irId = "hook-after-get_query_by_id_svc_iwa", hash = "8cc3c533", zone = 2)
    public void afterGetQueryById(String id) {
    }

    @MethodMetadata(irId = "list_querys_svc_em6", hash = "7ded3c08", zone = 1)
    public List<Query> listQuerys() {
        beforeListQuerys();
        try {
            return queryRepository.findAll();
        } catch (Exception e) {
            throw e;
        } finally {
            afterListQuerys();
        }
    }

    @MethodMetadata(irId = "hook-before-list_querys_svc_em6", hash = "143fb373", zone = 2)
    public void beforeListQuerys() {
    }

    @MethodMetadata(irId = "hook-after-list_querys_svc_em6", hash = "be5e076e", zone = 2)
    public void afterListQuerys() {
    }

    @MethodMetadata(irId = "update_query_svc_vhp", hash = "3279740a", zone = 1)
    public Query updateQuery(String id, UpdateQueryRequest request) {
        beforeUpdateQuery(id, request);
        try {
            Query existing = queryRepository.findById(id).orElseThrow(() -> new NotFoundException("Query not found with id: " + id));
            if (request.getApplicationId() != null) {
                Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
                existing.setApplication(application);
            }
            if (request.getCategory() != null) {
                existing.setCategory(request.getCategory());
            }
            if (request.getSubject() != null) {
                existing.setSubject(request.getSubject());
            }
            if (request.getStatus() != null) {
                existing.setStatus(request.getStatus());
            }
            return queryRepository.save(existing);
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateQuery(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_query_svc_vhp", hash = "bca8eefd", zone = 2)
    public void beforeUpdateQuery(String id, UpdateQueryRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_query_svc_vhp", hash = "b5bc1bc1", zone = 2)
    public void afterUpdateQuery(String id, UpdateQueryRequest request) {
    }

    @MethodMetadata(irId = "delete_query_svc_gco", hash = "24c909ef", zone = 1)
    public void deleteQuery(String id) {
        beforeDeleteQuery(id);
        try {
            queryRepository.findById(id).orElseThrow(() -> new NotFoundException("Query not found with id: " + id));
            queryRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteQuery(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_query_svc_gco", hash = "7ca49329", zone = 2)
    public void beforeDeleteQuery(String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_query_svc_gco", hash = "1c529823", zone = 2)
    public void afterDeleteQuery(String id) {
    }

    @Autowired()
    private QueryRepository queryRepository;

    @Autowired()
    private QueryMessageRepository queryMessageRepository;

    @Autowired()
    private KeycloakAuthService keycloakAuthService;

    /*
 * Operation    : Respond to Query and Update Status
 * Usecase ID   : UC-19
 * Usecase Name : Respond to Query / Update Status
 */
    @MethodMetadata(irId = "respondToQueryAndUpdateStatus_3sr", hash = "f2872252", zone = 1)
    public Void respondToQueryAndUpdateStatus(String queryId, String replyText, String newStatus, Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        Query query = queryRepository.findById(queryId).orElseThrow(() -> new RuntimeException("Query not found"));
        if (newStatus != null && !newStatus.isEmpty()) {
            query.setStatus(newStatus);
        }
        QueryMessage message = new QueryMessage();
        message.setId(java.util.UUID.randomUUID().toString());
        message.setQueryId(queryId);
        message.setSenderId(userId);
        message.setMessageText(replyText);
        message.setCreatedAt(LocalDateTime.now());
        queryMessageRepository.save(message);
        query.setUpdatedAt(LocalDateTime.now());
        queryRepository.save(query);
        return null;
    }
}
