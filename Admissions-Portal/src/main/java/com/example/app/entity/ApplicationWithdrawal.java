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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Table(name = "application_withdrawals", uniqueConstraints = { @UniqueConstraint(columnNames = { "application_id" }) })
public class ApplicationWithdrawal {

    @Id()
    @GeneratedValue()
    @UuidGenerator()
    @NotNull()
    private String id;

    @NotNull()
    @Column(name = "application_id", nullable = false)
    private String applicationId;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 200)
    private String name;

    @Column(nullable = false)
    @NotNull()
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    @Size(max = 254)
    private String email;

    @Column(nullable = false)
    @NotNull()
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String contact;

    @Column(nullable = false)
    @NotNull()
    private LocalDate dob;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 500)
    private String address;

    private String programme;

    @Min(value = 2000)
    @Max(value = 2100)
    private Integer year;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 500)
    private String reason;

    @ManyToOne()
    @JoinColumn(name = "application_id", insertable = false, updatable = false)
    private Application application;
}
