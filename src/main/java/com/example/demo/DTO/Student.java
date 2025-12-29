package com.example.demo.DTO;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class Student {

    // RESPONSE
    private UUID studentId;
    private String name;
    private Integer rollNumber;
    private UUID classId;

    private UUID schoolId;
    private UUID userID;

    // REQUEST ONLY
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String email;

    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    private String password;
    

    public Student(UUID studentId, String name, Integer rollNumber, UUID classId) {
        this.studentId = studentId;
        this.name = name;
        this.rollNumber = rollNumber;
        this.classId = classId;
    }




}
