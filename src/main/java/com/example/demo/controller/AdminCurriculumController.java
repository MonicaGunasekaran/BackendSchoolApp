package com.example.demo.controller;

import java.util.Map;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.CreateCurriculum;
import com.example.demo.DTO.CurriculumResponse;
import com.example.demo.common.ApiResponse;
import com.example.demo.entity.Curriculum;
import com.example.demo.service.AdminCurriculumService;

@RestController
@RequestMapping("/admin/curriculums")
public class AdminCurriculumController {

    private final AdminCurriculumService adminCurriculumService;

    public AdminCurriculumController(AdminCurriculumService adminCurriculumService) {
        this.adminCurriculumService = adminCurriculumService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Map<String,Object>> createCurriculum(
            @RequestBody CreateCurriculum request) {

        Curriculum curriculum =
                adminCurriculumService.createCurriculum(request);

        return ApiResponse.getResponse(
        		true,
                "Curriculum created successfully",
                curriculum
        );
    }
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllCurriculums() {

        List<CurriculumResponse> curriculums =
                adminCurriculumService.getAllCurriculums();

        return ApiResponse.getResponse(
        		true,
                "Curriculums fetched successfully",
                curriculums
        );
    }
}
