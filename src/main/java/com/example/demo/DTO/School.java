package com.example.demo.DTO;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class School {
    private String name;
    private String address;
    private UUID id; 
}
