package com.example.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "drives")
public class Drive {

    @Id()
    @GeneratedValue()
    @UuidGenerator()
    @NotNull()
    private String id;

    @Column(nullable = false)
    @NotNull()
    @Size(max = 200)
    private String name;

    @Column(nullable = false)
    @NotNull()
    private String driveType;

    @Column(nullable = false)
    @NotNull()
    private LocalDate startDate;

    @Column(nullable = false)
    @NotNull()
    private LocalDate endDate;

    private LocalDate editStartDate;

    private LocalDate editEndDate;

    private LocalDate jeeEditingEndDate;

    @Size(max = 500)
    private String details;
}
