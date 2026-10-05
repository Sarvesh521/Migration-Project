package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class CreateApplicationOfferHistoryRequest {

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

    private String offeredProgramme;

    public String getOfferedProgramme() {
        return this.offeredProgramme;
    }

    public void setOfferedProgramme(String offeredProgramme) {
        this.offeredProgramme = offeredProgramme;
    }

    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String slidingStatus;

    public String getSlidingStatus() {
        return this.slidingStatus;
    }

    public void setSlidingStatus(String slidingStatus) {
        this.slidingStatus = slidingStatus;
    }

    private String cancellationReason;

    public String getCancellationReason() {
        return this.cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    private String offerLetterDocId;

    public String getOfferLetterDocId() {
        return this.offerLetterDocId;
    }

    public void setOfferLetterDocId(String offerLetterDocId) {
        this.offerLetterDocId = offerLetterDocId;
    }
}
