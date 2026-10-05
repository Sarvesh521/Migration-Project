package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class RoundRequest {

    private String driveId;

    public String getDriveId() {
        return this.driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Integer roundNumber;

    public Integer getRoundNumber() {
        return this.roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    private String roundType;

    public String getRoundType() {
        return this.roundType;
    }

    public void setRoundType(String roundType) {
        this.roundType = roundType;
    }

    private LocalDate startDate;

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    private LocalDate endDate;

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    private LocalDate withdrawalEndDate;

    public LocalDate getWithdrawalEndDate() {
        return this.withdrawalEndDate;
    }

    public void setWithdrawalEndDate(LocalDate withdrawalEndDate) {
        this.withdrawalEndDate = withdrawalEndDate;
    }

    private LocalDate registrationEndDate;

    public LocalDate getRegistrationEndDate() {
        return this.registrationEndDate;
    }

    public void setRegistrationEndDate(LocalDate registrationEndDate) {
        this.registrationEndDate = registrationEndDate;
    }

    private String allocationType;

    public String getAllocationType() {
        return this.allocationType;
    }

    public void setAllocationType(String allocationType) {
        this.allocationType = allocationType;
    }

    private String allocationMode;

    public String getAllocationMode() {
        return this.allocationMode;
    }

    public void setAllocationMode(String allocationMode) {
        this.allocationMode = allocationMode;
    }

    private BigDecimal fee;

    public BigDecimal getFee() {
        return this.fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    private String offerLetterTemplateDocId;

    public String getOfferLetterTemplateDocId() {
        return this.offerLetterTemplateDocId;
    }

    public void setOfferLetterTemplateDocId(String offerLetterTemplateDocId) {
        this.offerLetterTemplateDocId = offerLetterTemplateDocId;
    }

    private Integer cseSeats;

    public Integer getCseSeats() {
        return this.cseSeats;
    }

    public void setCseSeats(Integer cseSeats) {
        this.cseSeats = cseSeats;
    }

    private Integer eceSeats;

    public Integer getEceSeats() {
        return this.eceSeats;
    }

    public void setEceSeats(Integer eceSeats) {
        this.eceSeats = eceSeats;
    }

    private Integer aiDsSeats;

    public Integer getAiDsSeats() {
        return this.aiDsSeats;
    }

    public void setAiDsSeats(Integer aiDsSeats) {
        this.aiDsSeats = aiDsSeats;
    }

    private Double btechCseCutoff;

    public Double getBtechCseCutoff() {
        return this.btechCseCutoff;
    }

    public void setBtechCseCutoff(Double btechCseCutoff) {
        this.btechCseCutoff = btechCseCutoff;
    }

    private Double imtechCseCutoff;

    public Double getImtechCseCutoff() {
        return this.imtechCseCutoff;
    }

    public void setImtechCseCutoff(Double imtechCseCutoff) {
        this.imtechCseCutoff = imtechCseCutoff;
    }

    private Double mtechCseCutoff;

    public Double getMtechCseCutoff() {
        return this.mtechCseCutoff;
    }

    public void setMtechCseCutoff(Double mtechCseCutoff) {
        this.mtechCseCutoff = mtechCseCutoff;
    }

    private Double mtechEceCutoff;

    public Double getMtechEceCutoff() {
        return this.mtechEceCutoff;
    }

    public void setMtechEceCutoff(Double mtechEceCutoff) {
        this.mtechEceCutoff = mtechEceCutoff;
    }

    private Double mtechAiDsCutoff;

    public Double getMtechAiDsCutoff() {
        return this.mtechAiDsCutoff;
    }

    public void setMtechAiDsCutoff(Double mtechAiDsCutoff) {
        this.mtechAiDsCutoff = mtechAiDsCutoff;
    }
}
