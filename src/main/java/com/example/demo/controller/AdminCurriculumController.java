package com.example.demo.controller;


import java.util.List;

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

    @PostMapping
    public ApiResponse<Curriculum> createCurriculum(
            @RequestBody CreateCurriculum request) {

        Curriculum curriculum =
                adminCurriculumService.createCurriculum(request);

        return ApiResponse.success(
                "Curriculum created successfully",
                curriculum
        );
    }
    @GetMapping
    public ApiResponse<List<CurriculumResponse>> getAllCurriculums() {

        List<CurriculumResponse> curriculums =
                adminCurriculumService.getAllCurriculums();

        return ApiResponse.success(
                "Curriculums fetched successfully",
                curriculums
        );
    }
}
