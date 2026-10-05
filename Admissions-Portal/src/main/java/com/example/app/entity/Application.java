package com.example.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity()
@Builder()
@Data()
@NoArgsConstructor()
@AllArgsConstructor()
@Getter()
@Setter()
@Table(name = "applications", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "drive_id" }) })
public class Application {

    @Id()
    @GeneratedValue()
    @UuidGenerator()
    @NotNull()
    private String id;

    @Column(name = "user_id", nullable = false)
    @NotNull()
    private String userId;

    @Column(name = "drive_id", nullable = false)
    @NotNull()
    private String driveId;

    @Column(nullable = false, unique = true)
    @NotNull()
    private String applicationNumber;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 200)
    private String fullName;

    @Column(nullable = false)
    @NotNull()
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @Size(max = 254)
    private String email;

    @Column(nullable = false)
    @NotNull()
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String mobile;

    @Column(nullable = false)
    @NotNull()
    private LocalDate dob;

    @Column(nullable = false)
    @NotNull()
    private String gender;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 100)
    private String nationality;

    @Size(max = 255)
    private String abcId;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 200)
    private String guardianName;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 100)
    private String guardianRelation;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String guardianMobile;

    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @Size(max = 254)
    private String guardianEmail;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String guardianAltMobile;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 500)
    private String currentAddressLine1;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 100)
    private String currentCity;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 100)
    private String currentState;

    @Pattern(regexp = "^\\d{6}$")
    private String currentPincode;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 500)
    private String permanentAddressLine1;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 100)
    private String permanentCity;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 100)
    private String permanentState;

    @Pattern(regexp = "^\\d{6}$")
    private String permanentPincode;

    @Column(nullable = false)
    @NotNull()
    private String status;

    @Column(nullable = false)
    @NotNull()
    private String jeeMainVerificationStatus;

    @Column(nullable = false)
    @NotNull()
    private String jeeAdvancedVerificationStatus;

    @Size(max = 500)
    private String adminRejectionReason;

    private String currentOfferedProgramme;

    private String currentOfferStatus;

    private String currentSlidingStatus;

    @ManyToOne()
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne()
    @JoinColumn(name = "drive_id", insertable = false, updatable = false)
    private Drive drive;
}
