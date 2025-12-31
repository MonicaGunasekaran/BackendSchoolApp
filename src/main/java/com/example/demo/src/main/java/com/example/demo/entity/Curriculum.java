package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
    name = "curriculum",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"grade", "section"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curriculum {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private Integer grade;

    // null for grades 1–10
    // A / B / C for 11 & 12
    private String section;
}
