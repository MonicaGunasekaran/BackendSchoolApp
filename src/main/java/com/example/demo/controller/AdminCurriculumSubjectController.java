package com.example.demo.controller;


import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.AddSubjectsToCurriculum;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.AdminCurriculumSubjectService;

@RestController
@RequestMapping("/admin/curriculums")
public class AdminCurriculumSubjectController {

    private final AdminCurriculumSubjectService service;

    public AdminCurriculumSubjectController(AdminCurriculumSubjectService service) {
        this.service = service;
    }

    @PostMapping("/{curriculumId}/subjects")
    public ResponseEntity<Map<String, Object>> addSubjects(
            @PathVariable UUID curriculumId,
            @RequestBody AddSubjectsToCurriculum request) {

        service.addSubjects(curriculumId, request);

        return ApiResponse.getResponse(
        		true,
                "Subjects added to curriculum successfully",
                null
        );
    }
}

