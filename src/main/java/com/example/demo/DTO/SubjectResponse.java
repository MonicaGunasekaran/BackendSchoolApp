package com.example.demo.DTO;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class SubjectResponse {
    private UUID id;
    private String name;
   
}
