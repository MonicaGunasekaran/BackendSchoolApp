package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TeacherResponse {
    private UUID id;
    private String email;
    private UUID schoolId;
}
