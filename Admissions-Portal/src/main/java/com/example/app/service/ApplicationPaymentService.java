package com.example.app.service;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateApplicationPaymentRequest;
import com.example.app.dto.UpdateApplicationPaymentRequest;
import com.example.app.entity.ApplicationPayment;
import com.example.app.entity.Application;
import com.example.app.entity.Round;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.ApplicationRepository;
import com.example.app.repository.RoundRepository;
import com.example.app.repository.ApplicationPaymentRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service()
public class ApplicationPaymentService {

    @Autowired()
    private ApplicationRepository applicationRepository;

    @Autowired()
    private RoundRepository roundRepository;

    @Autowired()
    private ApplicationPaymentRepository applicationPaymentRepository;

    @MethodMetadata(irId = "create_application_payment_svc_pvo", hash = "91aea8f3", zone = 1)
    public ApplicationPayment createApplicationPayment(CreateApplicationPaymentRequest request) {
        beforeCreateApplicationPayment(request);
        try {
            Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
            Round round = roundRepository.findById(request.getRoundId()).orElseThrow(() -> new NotFoundException("Round not found with id: " + request.getRoundId()));
            ApplicationPayment applicationPayment = new ApplicationPayment();
            applicationPayment.setPaymentType(request.getPaymentType());
            applicationPayment.setTransactionId(request.getTransactionId());
            applicationPayment.setReferenceNo(request.getReferenceNo());
            applicationPayment.setAmount(request.getAmount());
            applicationPayment.setPaymentStatus(request.getPaymentStatus());
            applicationPayment.setPaymentMode(request.getPaymentMode());
            applicationPayment.setPaymentGateway(request.getPaymentGateway());
            applicationPayment.setTransactionDate(request.getTransactionDate());
            applicationPayment.setResponseCode(request.getResponseCode());
            applicationPayment.setResponseMessage(request.getResponseMessage());
            applicationPayment.setOfferedPaymentProgramme(request.getOfferedPaymentProgramme());
            applicationPayment.setApplication(application);
            applicationPayment.setRound(round);
            return applicationPaymentRepository.save(applicationPayment);
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateApplicationPayment(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_application_payment_svc_pvo", hash = "93eccbce", zone = 2)
    public void beforeCreateApplicationPayment(CreateApplicationPaymentRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_application_payment_svc_pvo", hash = "bc21e0e1", zone = 2)
    public void afterCreateApplicationPayment(CreateApplicationPaymentRequest request) {
    }

    @MethodMetadata(irId = "get_application_payment_by_id_svc_gcg", hash = "9835d7c6", zone = 1)
    public ApplicationPayment getApplicationPaymentById(String id) {
        beforeGetApplicationPaymentById(id);
        try {
            return applicationPaymentRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationPayment not found with id: " + id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetApplicationPaymentById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_application_payment_by_id_svc_gcg", hash = "68e227fa", zone = 2)
    public void beforeGetApplicationPaymentById(String id) {
    }

    @MethodMetadata(irId = "hook-after-get_application_payment_by_id_svc_gcg", hash = "0b86f678", zone = 2)
    public void afterGetApplicationPaymentById(String id) {
    }

    @MethodMetadata(irId = "list_application_payments_svc_s8b", hash = "c6b0b3fd", zone = 1)
    public List<ApplicationPayment> listApplicationPayments() {
        beforeListApplicationPayments();
        try {
            return applicationPaymentRepository.findAll();
        } catch (Exception e) {
            throw e;
        } finally {
            afterListApplicationPayments();
        }
    }

    @MethodMetadata(irId = "hook-before-list_application_payments_svc_s8b", hash = "6ab21ba3", zone = 2)
    public void beforeListApplicationPayments() {
    }

    @MethodMetadata(irId = "hook-after-list_application_payments_svc_s8b", hash = "e8e4a001", zone = 2)
    public void afterListApplicationPayments() {
    }

    @MethodMetadata(irId = "update_application_payment_svc_g9n", hash = "a68b1e6f", zone = 1)
    public ApplicationPayment updateApplicationPayment(String id, UpdateApplicationPaymentRequest request) {
        beforeUpdateApplicationPayment(id, request);
        try {
            ApplicationPayment existing = applicationPaymentRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationPayment not found with id: " + id));
            if (request.getApplicationId() != null) {
                Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new NotFoundException("Application not found with id: " + request.getApplicationId()));
                existing.setApplication(application);
            }
            if (request.getRoundId() != null) {
                Round round = roundRepository.findById(request.getRoundId()).orElseThrow(() -> new NotFoundException("Round not found with id: " + request.getRoundId()));
                existing.setRound(round);
            }
            if (request.getPaymentType() != null) {
                existing.setPaymentType(request.getPaymentType());
            }
            if (request.getTransactionId() != null) {
                existing.setTransactionId(request.getTransactionId());
            }
            if (request.getReferenceNo() != null) {
                existing.setReferenceNo(request.getReferenceNo());
            }
            if (request.getAmount() != null) {
                existing.setAmount(request.getAmount());
            }
            if (request.getPaymentStatus() != null) {
                existing.setPaymentStatus(request.getPaymentStatus());
            }
            if (request.getPaymentMode() != null) {
                existing.setPaymentMode(request.getPaymentMode());
            }
            if (request.getPaymentGateway() != null) {
                existing.setPaymentGateway(request.getPaymentGateway());
            }
            if (request.getTransactionDate() != null) {
                existing.setTransactionDate(request.getTransactionDate());
            }
            if (request.getResponseCode() != null) {
                existing.setResponseCode(request.getResponseCode());
            }
            if (request.getResponseMessage() != null) {
                existing.setResponseMessage(request.getResponseMessage());
            }
            if (request.getOfferedPaymentProgramme() != null) {
                existing.setOfferedPaymentProgramme(request.getOfferedPaymentProgramme());
            }
            return applicationPaymentRepository.save(existing);
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateApplicationPayment(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_application_payment_svc_g9n", hash = "bb9e6ed3", zone = 2)
    public void beforeUpdateApplicationPayment(String id, UpdateApplicationPaymentRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_application_payment_svc_g9n", hash = "ece38fb4", zone = 2)
    public void afterUpdateApplicationPayment(String id, UpdateApplicationPaymentRequest request) {
    }

    @MethodMetadata(irId = "delete_application_payment_svc_1te", hash = "caad5d12", zone = 1)
    public void deleteApplicationPayment(String id) {
        beforeDeleteApplicationPayment(id);
        try {
            applicationPaymentRepository.findById(id).orElseThrow(() -> new NotFoundException("ApplicationPayment not found with id: " + id));
            applicationPaymentRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteApplicationPayment(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_application_payment_svc_1te", hash = "307297a2", zone = 2)
    public void beforeDeleteApplicationPayment(String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_application_payment_svc_1te", hash = "df2de416", zone = 2)
    public void afterDeleteApplicationPayment(String id) {
    }
}
