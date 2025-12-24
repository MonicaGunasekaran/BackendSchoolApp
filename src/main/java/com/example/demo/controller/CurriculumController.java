package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.CurriculumSubjectResponse;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.AdminCurriculumSubjectService;

@RestController
@RequestMapping("teacher/curriculums")
public class CurriculumController {

    private final AdminCurriculumSubjectService curriculumService;

    public CurriculumController(AdminCurriculumSubjectService curriculumService) {
        this.curriculumService = curriculumService;
    }

    @GetMapping("/{curriculumId}/subjects")
    public ApiResponse<List<CurriculumSubjectResponse>> getSubjects(
            @PathVariable UUID curriculumId) {

        List<CurriculumSubjectResponse> subjects =
                curriculumService.getSubjectsByCurriculum(curriculumId);

        return ApiResponse.success(
                "Subjects fetched successfully",
                subjects
        );
    }
}
