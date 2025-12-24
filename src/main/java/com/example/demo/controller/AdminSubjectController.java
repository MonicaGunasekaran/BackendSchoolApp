package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.demo.DTO.CreateSubject;
import com.example.demo.DTO.SubjectResponse;
import com.example.demo.common.ApiResponse;
import com.example.demo.entity.Subject;
import com.example.demo.service.AdminSubjectService;

@RestController
@RequestMapping("/admin/subjects")
public class AdminSubjectController {

    private final AdminSubjectService adminSubjectService;

    public AdminSubjectController(AdminSubjectService adminSubjectService) {
        this.adminSubjectService = adminSubjectService;
    }

    // ✅ CREATE SUBJECT
    @PostMapping
    public ApiResponse<Subject> createSubject(
            @RequestBody CreateSubject request) {

        Subject subject = adminSubjectService.createSubject(request);

        return ApiResponse.success(
                "Subject created successfully",
                subject
        );
    }

    // ✅ LIST SUBJECTS
    @GetMapping
    public ApiResponse<List<SubjectResponse>> getAllSubjects() {

        List<SubjectResponse> subjects =
                adminSubjectService.getAllSubjects();

        return ApiResponse.success(
                "Subjects fetched successfully",
                subjects
        );
    }
}
