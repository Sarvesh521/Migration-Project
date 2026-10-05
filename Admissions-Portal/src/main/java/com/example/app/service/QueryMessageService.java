package com.example.app.service;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateQueryMessageRequest;
import com.example.app.dto.UpdateQueryMessageRequest;
import com.example.app.entity.QueryMessage;
import com.example.app.entity.Query;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.QueryRepository;
import com.example.app.repository.QueryMessageRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service()
public class QueryMessageService {

    @Autowired()
    private QueryRepository queryRepository;

    @Autowired()
    private QueryMessageRepository queryMessageRepository;

    @MethodMetadata(irId = "create_query_message_svc_w99", hash = "e4e8cd43", zone = 1)
    public QueryMessage createQueryMessage(CreateQueryMessageRequest request) {
        beforeCreateQueryMessage(request);
        try {
            Query query = queryRepository.findById(request.getQueryId()).orElseThrow(() -> new NotFoundException("Query not found with id: " + request.getQueryId()));
            QueryMessage queryMessage = new QueryMessage();
            queryMessage.setSenderId(request.getSenderId());
            queryMessage.setMessageText(request.getMessageText());
            queryMessage.setAttachmentDocId(request.getAttachmentDocId());
            queryMessage.setQuery(query);
            return queryMessageRepository.save(queryMessage);
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateQueryMessage(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_query_message_svc_w99", hash = "d0cd9964", zone = 2)
    public void beforeCreateQueryMessage(CreateQueryMessageRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_query_message_svc_w99", hash = "c14e2020", zone = 2)
    public void afterCreateQueryMessage(CreateQueryMessageRequest request) {
    }

    @MethodMetadata(irId = "get_query_message_by_id_svc_bz5", hash = "0a651078", zone = 1)
    public QueryMessage getQueryMessageById(String id) {
        beforeGetQueryMessageById(id);
        try {
            return queryMessageRepository.findById(id).orElseThrow(() -> new NotFoundException("QueryMessage not found with id: " + id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetQueryMessageById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_query_message_by_id_svc_bz5", hash = "67247435", zone = 2)
    public void beforeGetQueryMessageById(String id) {
    }

    @MethodMetadata(irId = "hook-after-get_query_message_by_id_svc_bz5", hash = "52557790", zone = 2)
    public void afterGetQueryMessageById(String id) {
    }

    @MethodMetadata(irId = "list_query_messages_svc_4wx", hash = "0f92d663", zone = 1)
    public List<QueryMessage> listQueryMessages() {
        beforeListQueryMessages();
        try {
            return queryMessageRepository.findAll();
        } catch (Exception e) {
            throw e;
        } finally {
            afterListQueryMessages();
        }
    }

    @MethodMetadata(irId = "hook-before-list_query_messages_svc_4wx", hash = "114667e6", zone = 2)
    public void beforeListQueryMessages() {
    }

    @MethodMetadata(irId = "hook-after-list_query_messages_svc_4wx", hash = "ac9a737e", zone = 2)
    public void afterListQueryMessages() {
    }

    @MethodMetadata(irId = "update_query_message_svc_627", hash = "8a22b29b", zone = 1)
    public QueryMessage updateQueryMessage(String id, UpdateQueryMessageRequest request) {
        beforeUpdateQueryMessage(id, request);
        try {
            QueryMessage existing = queryMessageRepository.findById(id).orElseThrow(() -> new NotFoundException("QueryMessage not found with id: " + id));
            if (request.getQueryId() != null) {
                Query query = queryRepository.findById(request.getQueryId()).orElseThrow(() -> new NotFoundException("Query not found with id: " + request.getQueryId()));
                existing.setQuery(query);
            }
            if (request.getSenderId() != null) {
                existing.setSenderId(request.getSenderId());
            }
            if (request.getMessageText() != null) {
                existing.setMessageText(request.getMessageText());
            }
            if (request.getAttachmentDocId() != null) {
                existing.setAttachmentDocId(request.getAttachmentDocId());
            }
            return queryMessageRepository.save(existing);
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateQueryMessage(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_query_message_svc_627", hash = "66306200", zone = 2)
    public void beforeUpdateQueryMessage(String id, UpdateQueryMessageRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_query_message_svc_627", hash = "72423fdd", zone = 2)
    public void afterUpdateQueryMessage(String id, UpdateQueryMessageRequest request) {
    }

    @MethodMetadata(irId = "delete_query_message_svc_kwt", hash = "b0ddf40e", zone = 1)
    public void deleteQueryMessage(String id) {
        beforeDeleteQueryMessage(id);
        try {
            queryMessageRepository.findById(id).orElseThrow(() -> new NotFoundException("QueryMessage not found with id: " + id));
            queryMessageRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteQueryMessage(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_query_message_svc_kwt", hash = "68d437bb", zone = 2)
    public void beforeDeleteQueryMessage(String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_query_message_svc_kwt", hash = "b285442d", zone = 2)
    public void afterDeleteQueryMessage(String id) {
    }
}
