package com.example.demo.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;
@Data
@AllArgsConstructor
public class CurriculumSubjectResponse {
    private UUID subjectId;
    private String name;
    private Integer totalMarks;
}
