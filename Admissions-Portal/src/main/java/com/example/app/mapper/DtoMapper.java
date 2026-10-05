package com.example.app.mapper;

import com.example.app.dto.CreateApplicationDocumentRequest;
import com.example.app.dto.CreateApplicationEducationRequest;
import com.example.app.dto.CreateApplicationExamScoreRequest;
import com.example.app.dto.CreateApplicationOfferHistoryRequest;
import com.example.app.dto.CreateApplicationPaymentRequest;
import com.example.app.dto.CreateApplicationPreferenceRequest;
import com.example.app.dto.CreateApplicationRoundRequest;
import com.example.app.dto.CreateApplicationWithdrawalRequest;
import com.example.app.dto.CreateQueryMessageRequest;
import com.example.app.dto.CreateUserRequest;
import com.example.app.dto.UpdateApplicationDocumentRequest;
import com.example.app.dto.UpdateApplicationEducationRequest;
import com.example.app.dto.UpdateApplicationExamScoreRequest;
import com.example.app.dto.UpdateApplicationOfferHistoryRequest;
import com.example.app.dto.UpdateApplicationPaymentRequest;
import com.example.app.dto.UpdateApplicationPreferenceRequest;
import com.example.app.dto.UpdateApplicationRoundRequest;
import com.example.app.dto.UpdateQueryMessageRequest;
import com.example.app.dto.UpdateQueryRequest;
import com.example.app.dto.UpdateRoundRequest;
import com.example.app.dto.UpdateUserRequest;
import com.example.app.entity.ApplicationDocument;
import com.example.app.entity.ApplicationEducation;
import com.example.app.entity.ApplicationExamScore;
import com.example.app.entity.ApplicationOfferHistory;
import com.example.app.entity.ApplicationPayment;
import com.example.app.entity.ApplicationPreference;
import com.example.app.entity.ApplicationRound;
import com.example.app.entity.ApplicationWithdrawal;
import com.example.app.entity.Query;
import com.example.app.entity.QueryMessage;
import com.example.app.entity.Round;
import com.example.app.entity.User;
import org.springframework.stereotype.Component;

@Component()
public class DtoMapper {

    public User toUserFromCreateUserRequest(CreateUserRequest dto) {
        if (dto == null)
            return null;
        User entity = new User();
        entity.setEmail(dto.getEmail());
        entity.setPasswordHash(dto.getPasswordHash());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }

    public User toUserFromUpdateUserRequest(UpdateUserRequest dto) {
        if (dto == null)
            return null;
        User entity = new User();
        entity.setEmail(dto.getEmail());
        entity.setPasswordHash(dto.getPasswordHash());
        return entity;
    }

    public Round toRoundFromUpdateRoundRequest(UpdateRoundRequest dto) {
        if (dto == null)
            return null;
        Round entity = new Round();
        entity.setDriveId(dto.getDriveId());
        entity.setName(dto.getName());
        entity.setRoundNumber(dto.getRoundNumber());
        entity.setRoundType(dto.getRoundType());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setWithdrawalEndDate(dto.getWithdrawalEndDate());
        entity.setRegistrationEndDate(dto.getRegistrationEndDate());
        entity.setAllocationType(dto.getAllocationType());
        entity.setAllocationMode(dto.getAllocationMode());
        entity.setFee(dto.getFee());
        entity.setOfferLetterTemplateDocId(dto.getOfferLetterTemplateDocId());
        entity.setCseSeats(dto.getCseSeats());
        entity.setEceSeats(dto.getEceSeats());
        entity.setAiDsSeats(dto.getAiDsSeats());
        entity.setBtechCseCutoff(dto.getBtechCseCutoff());
        entity.setImtechCseCutoff(dto.getImtechCseCutoff());
        entity.setMtechCseCutoff(dto.getMtechCseCutoff());
        entity.setMtechEceCutoff(dto.getMtechEceCutoff());
        entity.setMtechAiDsCutoff(dto.getMtechAiDsCutoff());
        return entity;
    }

    public ApplicationEducation toApplicationEducationFromCreateApplicationEducationRequest(CreateApplicationEducationRequest dto) {
        if (dto == null)
            return null;
        ApplicationEducation entity = new ApplicationEducation();
        entity.setApplicationId(dto.getApplicationId());
        entity.setEducationLevel(dto.getEducationLevel());
        entity.setBoardUniversity(dto.getBoardUniversity());
        entity.setInstitutionName(dto.getInstitutionName());
        entity.setSpecialization(dto.getSpecialization());
        return entity;
    }

    public ApplicationEducation toApplicationEducationFromUpdateApplicationEducationRequest(UpdateApplicationEducationRequest dto) {
        if (dto == null)
            return null;
        ApplicationEducation entity = new ApplicationEducation();
        entity.setApplicationId(dto.getApplicationId());
        entity.setEducationLevel(dto.getEducationLevel());
        entity.setBoardUniversity(dto.getBoardUniversity());
        entity.setInstitutionName(dto.getInstitutionName());
        entity.setSpecialization(dto.getSpecialization());
        return entity;
    }

    public ApplicationExamScore toApplicationExamScoreFromCreateApplicationExamScoreRequest(CreateApplicationExamScoreRequest dto) {
        if (dto == null)
            return null;
        ApplicationExamScore entity = new ApplicationExamScore();
        entity.setApplicationId(dto.getApplicationId());
        entity.setExamType(dto.getExamType());
        entity.setRollNumber(dto.getRollNumber());
        entity.setScore(dto.getScore());
        entity.setRank(dto.getRank());
        entity.setExamYear(dto.getExamYear());
        entity.setSubjects(dto.getSubjects());
        return entity;
    }

    public ApplicationExamScore toApplicationExamScoreFromUpdateApplicationExamScoreRequest(UpdateApplicationExamScoreRequest dto) {
        if (dto == null)
            return null;
        ApplicationExamScore entity = new ApplicationExamScore();
        entity.setApplicationId(dto.getApplicationId());
        entity.setExamType(dto.getExamType());
        entity.setRollNumber(dto.getRollNumber());
        entity.setScore(dto.getScore());
        entity.setRank(dto.getRank());
        entity.setExamYear(dto.getExamYear());
        entity.setSubjects(dto.getSubjects());
        return entity;
    }

    public ApplicationPreference toApplicationPreferenceFromCreateApplicationPreferenceRequest(CreateApplicationPreferenceRequest dto) {
        if (dto == null)
            return null;
        ApplicationPreference entity = new ApplicationPreference();
        entity.setApplicationId(dto.getApplicationId());
        entity.setPreferenceOrder(dto.getPreferenceOrder());
        entity.setProgramme(dto.getProgramme());
        return entity;
    }

    public ApplicationPreference toApplicationPreferenceFromUpdateApplicationPreferenceRequest(UpdateApplicationPreferenceRequest dto) {
        if (dto == null)
            return null;
        ApplicationPreference entity = new ApplicationPreference();
        entity.setApplicationId(dto.getApplicationId());
        entity.setPreferenceOrder(dto.getPreferenceOrder());
        entity.setProgramme(dto.getProgramme());
        return entity;
    }

    public ApplicationDocument toApplicationDocumentFromCreateApplicationDocumentRequest(CreateApplicationDocumentRequest dto) {
        if (dto == null)
            return null;
        ApplicationDocument entity = new ApplicationDocument();
        entity.setApplicationId(dto.getApplicationId());
        entity.setDocumentType(dto.getDocumentType());
        entity.setFileReference(dto.getFileReference());
        entity.setRoundId(dto.getRoundId());
        entity.setUploadedBy(dto.getUploadedBy());
        return entity;
    }

    public ApplicationDocument toApplicationDocumentFromUpdateApplicationDocumentRequest(UpdateApplicationDocumentRequest dto) {
        if (dto == null)
            return null;
        ApplicationDocument entity = new ApplicationDocument();
        entity.setApplicationId(dto.getApplicationId());
        entity.setDocumentType(dto.getDocumentType());
        entity.setFileReference(dto.getFileReference());
        entity.setRoundId(dto.getRoundId());
        entity.setUploadedBy(dto.getUploadedBy());
        return entity;
    }

    public ApplicationRound toApplicationRoundFromCreateApplicationRoundRequest(CreateApplicationRoundRequest dto) {
        if (dto == null)
            return null;
        ApplicationRound entity = new ApplicationRound();
        entity.setApplicationId(dto.getApplicationId());
        entity.setRoundId(dto.getRoundId());
        entity.setAllocatedProgramme(dto.getAllocatedProgramme());
        entity.setOfferStatus(dto.getOfferStatus());
        entity.setRoundStatus(dto.getRoundStatus());
        entity.setSlidingStatus(dto.getSlidingStatus());
        entity.setToken(dto.getToken());
        entity.setPaymentStatus(dto.getPaymentStatus());
        entity.setOfferLetterDocId(dto.getOfferLetterDocId());
        return entity;
    }

    public ApplicationRound toApplicationRoundFromUpdateApplicationRoundRequest(UpdateApplicationRoundRequest dto) {
        if (dto == null)
            return null;
        ApplicationRound entity = new ApplicationRound();
        entity.setApplicationId(dto.getApplicationId());
        entity.setRoundId(dto.getRoundId());
        entity.setAllocatedProgramme(dto.getAllocatedProgramme());
        entity.setOfferStatus(dto.getOfferStatus());
        entity.setRoundStatus(dto.getRoundStatus());
        entity.setSlidingStatus(dto.getSlidingStatus());
        entity.setToken(dto.getToken());
        entity.setPaymentStatus(dto.getPaymentStatus());
        entity.setOfferLetterDocId(dto.getOfferLetterDocId());
        return entity;
    }

    public ApplicationOfferHistory toApplicationOfferHistoryFromCreateApplicationOfferHistoryRequest(CreateApplicationOfferHistoryRequest dto) {
        if (dto == null)
            return null;
        ApplicationOfferHistory entity = new ApplicationOfferHistory();
        entity.setApplicationId(dto.getApplicationId());
        entity.setRoundId(dto.getRoundId());
        entity.setOfferedProgramme(dto.getOfferedProgramme());
        entity.setStatus(dto.getStatus());
        entity.setSlidingStatus(dto.getSlidingStatus());
        entity.setCancellationReason(dto.getCancellationReason());
        entity.setOfferLetterDocId(dto.getOfferLetterDocId());
        return entity;
    }

    public ApplicationOfferHistory toApplicationOfferHistoryFromUpdateApplicationOfferHistoryRequest(UpdateApplicationOfferHistoryRequest dto) {
        if (dto == null)
            return null;
        ApplicationOfferHistory entity = new ApplicationOfferHistory();
        entity.setApplicationId(dto.getApplicationId());
        entity.setRoundId(dto.getRoundId());
        entity.setOfferedProgramme(dto.getOfferedProgramme());
        entity.setStatus(dto.getStatus());
        entity.setSlidingStatus(dto.getSlidingStatus());
        entity.setCancellationReason(dto.getCancellationReason());
        entity.setOfferLetterDocId(dto.getOfferLetterDocId());
        return entity;
    }

    public ApplicationPayment toApplicationPaymentFromCreateApplicationPaymentRequest(CreateApplicationPaymentRequest dto) {
        if (dto == null)
            return null;
        ApplicationPayment entity = new ApplicationPayment();
        entity.setApplicationId(dto.getApplicationId());
        entity.setRoundId(dto.getRoundId());
        entity.setPaymentType(dto.getPaymentType());
        entity.setTransactionId(dto.getTransactionId());
        entity.setReferenceNo(dto.getReferenceNo());
        entity.setAmount(dto.getAmount());
        entity.setPaymentStatus(dto.getPaymentStatus());
        entity.setPaymentMode(dto.getPaymentMode());
        entity.setPaymentGateway(dto.getPaymentGateway());
        entity.setTransactionDate(dto.getTransactionDate());
        entity.setResponseCode(dto.getResponseCode());
        entity.setResponseMessage(dto.getResponseMessage());
        entity.setOfferedPaymentProgramme(dto.getOfferedPaymentProgramme());
        return entity;
    }

    public ApplicationPayment toApplicationPaymentFromUpdateApplicationPaymentRequest(UpdateApplicationPaymentRequest dto) {
        if (dto == null)
            return null;
        ApplicationPayment entity = new ApplicationPayment();
        entity.setApplicationId(dto.getApplicationId());
        entity.setRoundId(dto.getRoundId());
        entity.setPaymentType(dto.getPaymentType());
        entity.setTransactionId(dto.getTransactionId());
        entity.setReferenceNo(dto.getReferenceNo());
        entity.setAmount(dto.getAmount());
        entity.setPaymentStatus(dto.getPaymentStatus());
        entity.setPaymentMode(dto.getPaymentMode());
        entity.setPaymentGateway(dto.getPaymentGateway());
        entity.setTransactionDate(dto.getTransactionDate());
        entity.setResponseCode(dto.getResponseCode());
        entity.setResponseMessage(dto.getResponseMessage());
        entity.setOfferedPaymentProgramme(dto.getOfferedPaymentProgramme());
        return entity;
    }

    public ApplicationWithdrawal toApplicationWithdrawalFromCreateApplicationWithdrawalRequest(CreateApplicationWithdrawalRequest dto) {
        if (dto == null)
            return null;
        ApplicationWithdrawal entity = new ApplicationWithdrawal();
        entity.setApplicationId(dto.getApplicationId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setContact(dto.getContact());
        entity.setDob(dto.getDob());
        entity.setAddress(dto.getAddress());
        entity.setProgramme(dto.getProgramme());
        entity.setYear(dto.getYear());
        entity.setReason(dto.getReason());
        return entity;
    }

    public Query toQueryFromUpdateQueryRequest(UpdateQueryRequest dto) {
        if (dto == null)
            return null;
        Query entity = new Query();
        entity.setApplicationId(dto.getApplicationId());
        entity.setCategory(dto.getCategory());
        entity.setSubject(dto.getSubject());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public QueryMessage toQueryMessageFromCreateQueryMessageRequest(CreateQueryMessageRequest dto) {
        if (dto == null)
            return null;
        QueryMessage entity = new QueryMessage();
        entity.setQueryId(dto.getQueryId());
        entity.setSenderId(dto.getSenderId());
        entity.setMessageText(dto.getMessageText());
        entity.setAttachmentDocId(dto.getAttachmentDocId());
        return entity;
    }

    public QueryMessage toQueryMessageFromUpdateQueryMessageRequest(UpdateQueryMessageRequest dto) {
        if (dto == null)
            return null;
        QueryMessage entity = new QueryMessage();
        entity.setQueryId(dto.getQueryId());
        entity.setSenderId(dto.getSenderId());
        entity.setMessageText(dto.getMessageText());
        entity.setAttachmentDocId(dto.getAttachmentDocId());
        return entity;
    }
}
