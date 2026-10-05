package com.example.app.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class ApplicationDetailsResponse {

    private ApplicationDto application;

    public ApplicationDto getApplication() {
        return this.application;
    }

    public void setApplication(ApplicationDto application) {
        this.application = application;
    }

    private List<ApplicationEducationDto> educations;

    public List<ApplicationEducationDto> getEducations() {
        return this.educations;
    }

    public void setEducations(List<ApplicationEducationDto> educations) {
        this.educations = educations;
    }

    private List<ApplicationExamScoreDto> examScores;

    public List<ApplicationExamScoreDto> getExamScores() {
        return this.examScores;
    }

    public void setExamScores(List<ApplicationExamScoreDto> examScores) {
        this.examScores = examScores;
    }

    private List<ApplicationPreferenceDto> preferences;

    public List<ApplicationPreferenceDto> getPreferences() {
        return this.preferences;
    }

    public void setPreferences(List<ApplicationPreferenceDto> preferences) {
        this.preferences = preferences;
    }

    private List<ApplicationDocumentDto> documents;

    public List<ApplicationDocumentDto> getDocuments() {
        return this.documents;
    }

    public void setDocuments(List<ApplicationDocumentDto> documents) {
        this.documents = documents;
    }

    private List<ApplicationRoundDto> rounds;

    public List<ApplicationRoundDto> getRounds() {
        return this.rounds;
    }

    public void setRounds(List<ApplicationRoundDto> rounds) {
        this.rounds = rounds;
    }

    private List<ApplicationOfferHistoryDto> offerHistories;

    public List<ApplicationOfferHistoryDto> getOfferHistories() {
        return this.offerHistories;
    }

    public void setOfferHistories(List<ApplicationOfferHistoryDto> offerHistories) {
        this.offerHistories = offerHistories;
    }

    private List<ApplicationPaymentDto> payments;

    public List<ApplicationPaymentDto> getPayments() {
        return this.payments;
    }

    public void setPayments(List<ApplicationPaymentDto> payments) {
        this.payments = payments;
    }

    private List<QueryDto> queries;

    public List<QueryDto> getQueries() {
        return this.queries;
    }

    public void setQueries(List<QueryDto> queries) {
        this.queries = queries;
    }
}
