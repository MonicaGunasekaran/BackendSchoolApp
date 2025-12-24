package com.example.demo.controller;


import com.example.demo.DTO.AddSubjectsToCurriculum;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.AdminCurriculumSubjectService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/curriculums")
public class AdminCurriculumSubjectController {

    private final AdminCurriculumSubjectService service;

    public AdminCurriculumSubjectController(AdminCurriculumSubjectService service) {
        this.service = service;
    }

    @PostMapping("/{curriculumId}/subjects")
    public ApiResponse<Void> addSubjects(
            @PathVariable UUID curriculumId,
            @RequestBody AddSubjectsToCurriculum request) {

        service.addSubjects(curriculumId, request);

        return ApiResponse.success(
                "Subjects added to curriculum successfully",
                null
        );
    }
}

