package com.example.demo.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.CreateTeacher;
import com.example.demo.DTO.TeacherResponse;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.AdminTeacherService;

@RestController
@RequestMapping("/admin/schools/{schoolId}/teachers")
public class AdminTeacherController {

    private final AdminTeacherService adminTeacherService;

    public AdminTeacherController(AdminTeacherService adminTeacherService) {
        this.adminTeacherService = adminTeacherService;
    }
    /**/

    @PostMapping
    public ApiResponse<TeacherResponse> createTeacher(
            @PathVariable UUID schoolId,
            @RequestBody CreateTeacher request) {
        TeacherResponse response =
                adminTeacherService.createTeacher(schoolId, request);

            return ApiResponse.success(
                "Teacher created successfully",
                response
            );

    }
}
