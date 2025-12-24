package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.StudentResultResponse;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.StudentResultService;

@RestController
@RequestMapping("/student")
public class StudentResultController {

    private final StudentResultService studentResultService;

    public StudentResultController(StudentResultService studentResultService) {
        this.studentResultService = studentResultService;
    }

    @GetMapping("/results")
    public ApiResponse<StudentResultResponse> getResults() {
        StudentResultResponse response =
                studentResultService.getStudentResults();

        return ApiResponse.success(
                "Results fetched successfully",
                response
        );
    }
}
