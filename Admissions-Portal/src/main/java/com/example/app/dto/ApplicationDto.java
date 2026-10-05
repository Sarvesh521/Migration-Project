package com.example.app.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class ApplicationDto {

    private String id;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String userId;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String driveId;

    public String getDriveId() {
        return this.driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    private String applicationNumber;

    public String getApplicationNumber() {
        return this.applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    private String fullName;

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private String email;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String mobile;

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private LocalDate dob;

    public LocalDate getDob() {
        return this.dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    private String gender;

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String nationality;

    public String getNationality() {
        return this.nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    private String abcId;

    public String getAbcId() {
        return this.abcId;
    }

    public void setAbcId(String abcId) {
        this.abcId = abcId;
    }

    private String guardianName;

    public String getGuardianName() {
        return this.guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    private String guardianRelation;

    public String getGuardianRelation() {
        return this.guardianRelation;
    }

    public void setGuardianRelation(String guardianRelation) {
        this.guardianRelation = guardianRelation;
    }

    private String guardianMobile;

    public String getGuardianMobile() {
        return this.guardianMobile;
    }

    public void setGuardianMobile(String guardianMobile) {
        this.guardianMobile = guardianMobile;
    }

    private String guardianEmail;

    public String getGuardianEmail() {
        return this.guardianEmail;
    }

    public void setGuardianEmail(String guardianEmail) {
        this.guardianEmail = guardianEmail;
    }

    private String guardianAltMobile;

    public String getGuardianAltMobile() {
        return this.guardianAltMobile;
    }

    public void setGuardianAltMobile(String guardianAltMobile) {
        this.guardianAltMobile = guardianAltMobile;
    }

    private String currentAddressLine1;

    public String getCurrentAddressLine1() {
        return this.currentAddressLine1;
    }

    public void setCurrentAddressLine1(String currentAddressLine1) {
        this.currentAddressLine1 = currentAddressLine1;
    }

    private String currentCity;

    public String getCurrentCity() {
        return this.currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    private String currentState;

    public String getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    private String currentPincode;

    public String getCurrentPincode() {
        return this.currentPincode;
    }

    public void setCurrentPincode(String currentPincode) {
        this.currentPincode = currentPincode;
    }

    private String permanentAddressLine1;

    public String getPermanentAddressLine1() {
        return this.permanentAddressLine1;
    }

    public void setPermanentAddressLine1(String permanentAddressLine1) {
        this.permanentAddressLine1 = permanentAddressLine1;
    }

    private String permanentCity;

    public String getPermanentCity() {
        return this.permanentCity;
    }

    public void setPermanentCity(String permanentCity) {
        this.permanentCity = permanentCity;
    }

    private String permanentState;

    public String getPermanentState() {
        return this.permanentState;
    }

    public void setPermanentState(String permanentState) {
        this.permanentState = permanentState;
    }

    private String permanentPincode;

    public String getPermanentPincode() {
        return this.permanentPincode;
    }

    public void setPermanentPincode(String permanentPincode) {
        this.permanentPincode = permanentPincode;
    }

    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String jeeMainVerificationStatus;

    public String getJeeMainVerificationStatus() {
        return this.jeeMainVerificationStatus;
    }

    public void setJeeMainVerificationStatus(String jeeMainVerificationStatus) {
        this.jeeMainVerificationStatus = jeeMainVerificationStatus;
    }

    private String jeeAdvancedVerificationStatus;

    public String getJeeAdvancedVerificationStatus() {
        return this.jeeAdvancedVerificationStatus;
    }

    public void setJeeAdvancedVerificationStatus(String jeeAdvancedVerificationStatus) {
        this.jeeAdvancedVerificationStatus = jeeAdvancedVerificationStatus;
    }

    private String adminRejectionReason;

    public String getAdminRejectionReason() {
        return this.adminRejectionReason;
    }

    public void setAdminRejectionReason(String adminRejectionReason) {
        this.adminRejectionReason = adminRejectionReason;
    }

    private String currentOfferedProgramme;

    public String getCurrentOfferedProgramme() {
        return this.currentOfferedProgramme;
    }

    public void setCurrentOfferedProgramme(String currentOfferedProgramme) {
        this.currentOfferedProgramme = currentOfferedProgramme;
    }

    private String currentOfferStatus;

    public String getCurrentOfferStatus() {
        return this.currentOfferStatus;
    }

    public void setCurrentOfferStatus(String currentOfferStatus) {
        this.currentOfferStatus = currentOfferStatus;
    }

    private String currentSlidingStatus;

    public String getCurrentSlidingStatus() {
        return this.currentSlidingStatus;
    }

    public void setCurrentSlidingStatus(String currentSlidingStatus) {
        this.currentSlidingStatus = currentSlidingStatus;
    }
}
