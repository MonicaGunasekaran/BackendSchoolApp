package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(
    name = "class_rooms",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"school_id", "grade", "section"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassRoom {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private Integer grade;
    @Column(nullable = false)
    private String section;
    @Column(name = "school_id", nullable = false)
    private UUID schoolId;
    @Column(name = "teacher_id", nullable = false)
    private UUID teacherId;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    @Column(nullable = false)
    private UUID curriculumId;
}
