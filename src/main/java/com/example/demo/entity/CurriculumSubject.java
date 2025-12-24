package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
    name = "curriculum_subjects",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"curriculum_id", "subject_id"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurriculumSubject {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "curriculum_id")
    private Curriculum curriculum;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id")
    private Subject subject;
}
