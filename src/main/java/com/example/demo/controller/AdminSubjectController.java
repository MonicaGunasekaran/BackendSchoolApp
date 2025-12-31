package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Map<String,Object>> createSubject(
            @RequestBody CreateSubject request) {

        Subject subject = adminSubjectService.createSubject(request);
        return ApiResponse.getResponse(
        		true,
                "Subject created successfully",
                subject
        );
    }
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllSubjects() {
        List<SubjectResponse> subjects =
                adminSubjectService.getAllSubjects();
        return ApiResponse.getResponse(true,
                "Subjects fetched successfully",
                subjects
        );
    }
}
