package com.example.demo.DTO;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class ClassRoomResponse {
    private UUID classRoomId;
    private String grade;
    private String section;
}
