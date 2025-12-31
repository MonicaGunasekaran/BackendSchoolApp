package com.example.demo.DTO;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class StudentResultResponse {
    private String studentName;
    private Integer rollNumber;
    private UUID classId;
    private List<SubjectResult> subjects;
    private String status;
}