package com.example.demo.DTO;

import lombok.Data;

@Data
public class CreateStudent {

    private String name;
    private Integer rollNumber;
    private String email;
    private String password;
}
