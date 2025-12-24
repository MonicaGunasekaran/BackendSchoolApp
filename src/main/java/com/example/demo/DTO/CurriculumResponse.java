package com.example.demo.DTO;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurriculumResponse {
    private UUID id;
    private Integer grade;
    private String section;
}
