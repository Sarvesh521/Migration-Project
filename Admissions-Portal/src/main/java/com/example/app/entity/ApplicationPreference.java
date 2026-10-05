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
@Table(name = "application_preferences", uniqueConstraints = { @UniqueConstraint(columnNames = { "application_id", "preference_order" }) })
public class ApplicationPreference {

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
    @Min(value = 1)
    private Integer preferenceOrder;

    @Column(nullable = false)
    @NotNull()
    private String programme;

    @ManyToOne()
    @JoinColumn(name = "application_id", insertable = false, updatable = false)
    private Application application;
}
