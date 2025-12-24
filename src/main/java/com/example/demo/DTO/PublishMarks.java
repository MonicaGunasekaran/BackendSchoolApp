package com.example.demo.DTO;


import lombok.Data;
import java.util.UUID;

@Data
public class PublishMarks {
    private UUID subjectId;
    private Integer marks;
}
