package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.CurriculumSubjectResponse;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.AdminCurriculumSubjectService;

@RestController
@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
@RequestMapping("teacher/curriculums")
public class CurriculumController {

    private final AdminCurriculumSubjectService curriculumService;

    public CurriculumController(AdminCurriculumSubjectService curriculumService) {
        this.curriculumService = curriculumService;
    }
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/{curriculumId}/subjects")
    public ResponseEntity<Map<String,Object>> getSubjects(
            @PathVariable UUID curriculumId) {

        List<CurriculumSubjectResponse> subjects =
                curriculumService.getSubjectsByCurriculum(curriculumId);

        return ApiResponse.getResponse(
                true,
                "Subjects fetched successfully",
                subjects
        );
    }
}
