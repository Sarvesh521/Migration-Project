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
@Table(name = "application_exam_scores", uniqueConstraints = { @UniqueConstraint(columnNames = { "application_id", "exam_type" }) })
public class ApplicationExamScore {

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
    private String examType;

    @Size(max = 50)
    private String rollNumber;

    @Min(value = 0)
    private Double score;

    @Min(value = 1)
    @Column(name = "exam_rank")
    private Integer rank;

    @Column(nullable = false)
    @NotNull()
    @Min(value = 2000)
    @Max(value = 2100)
    private Integer examYear;

    private String subjects;

    @ManyToOne()
    @JoinColumn(name = "application_id", insertable = false, updatable = false)
    private Application application;
}
