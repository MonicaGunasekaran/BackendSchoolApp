package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(
    name = "marks",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"student_id", "subject_id"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkEntity {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "student_id", nullable = false)
    private UUID studentId;
    @Column(name = "class_id", nullable = false)
    private UUID classId;
    @Column(name = "subject_id", nullable = false)
    private UUID subjectId;
    @Column(nullable = false)
    private Integer marks;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
