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
import jakarta.validation.constraints.Size;
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
@Table(name = "application_educations", uniqueConstraints = { @UniqueConstraint(columnNames = { "application_id", "education_level" }) })
public class ApplicationEducation {

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
    private String educationLevel;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 255)
    private String boardUniversity;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 255)
    private String institutionName;

    @Size(max = 255)
    private String specialization;

    @ManyToOne()
    @JoinColumn(name = "application_id", insertable = false, updatable = false)
    private Application application;
}
