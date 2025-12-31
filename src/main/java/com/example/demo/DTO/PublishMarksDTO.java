package com.example.demo.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishMarksDTO {
    private List<SubjectMark> subjects;
    private UUID subjectId;
    private Integer studentId;
    private Integer marks;
}
