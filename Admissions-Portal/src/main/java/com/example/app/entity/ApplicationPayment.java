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
import java.time.LocalDateTime;
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
@Table(name = "application_payments", uniqueConstraints = { @UniqueConstraint(columnNames = { "transaction_id" }) })
public class ApplicationPayment {

    @Id()
    @GeneratedValue()
    @UuidGenerator()
    @NotNull()
    private String id;

    @NotNull()
    @Column(name = "application_id", nullable = false)
    private String applicationId;

    @Column(name = "round_id", nullable = false)
    private String roundId;

    private String paymentType;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 255)
    private String transactionId;

    @Size(max = 255)
    private String referenceNo;

    @Min(value = 0)
    private BigDecimal amount;

    private String paymentStatus;

    @Size(max = 50)
    private String paymentMode;

    private String paymentGateway;

    private LocalDateTime paymentDate;

    private LocalDateTime transactionDate;

    @Size(max = 50)
    private String responseCode;

    @Size(max = 255)
    private String responseMessage;

    private String offeredPaymentProgramme;

    @ManyToOne()
    @JoinColumn(name = "application_id", insertable = false, updatable = false)
    private Application application;

    @ManyToOne()
    @JoinColumn(name = "round_id", insertable = false, updatable = false)
    private Round round;
}
