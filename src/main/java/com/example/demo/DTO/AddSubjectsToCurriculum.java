package com.example.demo.DTO;
import lombok.Data;
import java.util.List;
import java.util.UUID;
@Data
public class AddSubjectsToCurriculum{
    private List<UUID> subjectIds;
}