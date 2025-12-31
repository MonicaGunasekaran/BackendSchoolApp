package com.example.demo.DTO;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MarkResponse {
    private UUID subjectId;
    private String subjectName;
    private Integer marks;
}
