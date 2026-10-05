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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
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
@Table(name = "rounds", uniqueConstraints = { @UniqueConstraint(columnNames = { "drive_id", "round_number" }) })
public class Round {

    @Id()
    @GeneratedValue()
    @UuidGenerator()
    @NotNull()
    private String id;

    @Column(name = "drive_id", nullable = false)
    @NotNull()
    private String driveId;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 200)
    private String name;

    @Column(nullable = false)
    @NotNull()
    @Min(value = 1)
    private Integer roundNumber;

    @Column(nullable = false)
    @NotNull()
    private String roundType;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate withdrawalEndDate;

    private LocalDate registrationEndDate;

    private String allocationType;

    private String allocationMode;

    @Min(value = 0)
    private BigDecimal fee;

    @Size(max = 255)
    private String offerLetterTemplateDocId;

    @Min(value = 0)
    private Integer cseSeats;

    @Min(value = 0)
    private Integer eceSeats;

    @Min(value = 0)
    private Integer aiDsSeats;

    @Column(nullable = false)
    @NotNull()
    @Min(value = 0)
    private Integer remainingCseSeats;

    @Column(nullable = false)
    @NotNull()
    @Min(value = 0)
    private Integer remainingEceSeats;

    @Column(nullable = false)
    @NotNull()
    @Min(value = 0)
    private Integer remainingAiDsSeats;

    @Column(nullable = false)
    @NotNull()
    private Boolean btcseExhausted;

    @Column(nullable = false)
    @NotNull()
    private Boolean bteceExhausted;

    @Column(nullable = false)
    @NotNull()
    private Boolean btcseAiDsExhausted;

    @Column(nullable = false)
    @NotNull()
    private Boolean mteceExhausted;

    @Column(nullable = false)
    @NotNull()
    private Boolean mtaiDsExhausted;

    @Min(value = 0)
    private Double btechCseCutoff;

    @Min(value = 0)
    private Double imtechCseCutoff;

    @Min(value = 0)
    private Double mtechCseCutoff;

    @Min(value = 0)
    private Double mtechEceCutoff;

    @Min(value = 0)
    private Double mtechAiDsCutoff;

    @ManyToOne()
    @JoinColumn(name = "drive_id", insertable = false, updatable = false)
    private Drive drive;
}
