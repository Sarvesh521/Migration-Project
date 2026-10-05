package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class UpdateApplicationRoundRequest {

    private String applicationId;

    public String getApplicationId() {
        return this.applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    private String roundId;

    public String getRoundId() {
        return this.roundId;
    }

    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }

    private String allocatedProgramme;

    public String getAllocatedProgramme() {
        return this.allocatedProgramme;
    }

    public void setAllocatedProgramme(String allocatedProgramme) {
        this.allocatedProgramme = allocatedProgramme;
    }

    private String offerStatus;

    public String getOfferStatus() {
        return this.offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    private String roundStatus;

    public String getRoundStatus() {
        return this.roundStatus;
    }

    public void setRoundStatus(String roundStatus) {
        this.roundStatus = roundStatus;
    }

    private String slidingStatus;

    public String getSlidingStatus() {
        return this.slidingStatus;
    }

    public void setSlidingStatus(String slidingStatus) {
        this.slidingStatus = slidingStatus;
    }

    private String token;

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String paymentStatus;

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    private String offerLetterDocId;

    public String getOfferLetterDocId() {
        return this.offerLetterDocId;
    }

    public void setOfferLetterDocId(String offerLetterDocId) {
        this.offerLetterDocId = offerLetterDocId;
    }
}
