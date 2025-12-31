package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(
    name = "student_profiles",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"class_id", "roll_number"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentEntity {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(name = "roll_number", nullable = false)
    private Integer rollNumber;
    @Column(name = "class_id", nullable = false)
    private UUID classId;
    @Column(name = "school_id", nullable = false)
    private UUID schoolId;
    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
