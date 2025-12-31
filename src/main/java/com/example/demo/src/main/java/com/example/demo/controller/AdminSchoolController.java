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

import com.example.demo.DTO.School;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.AdminSchoolService;

@RestController
@RequestMapping("/admin/schools")
public class AdminSchoolController {

    private final AdminSchoolService adminSchoolService;

    public AdminSchoolController(AdminSchoolService adminSchoolService) {
        this.adminSchoolService = adminSchoolService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createSchool(@RequestBody School request) {
        School response=adminSchoolService.createSchool(request);
        return ApiResponse.getResponse(true,"School created successfully", response);
    }
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllSchools() {
        List<School> schools =
                adminSchoolService.getAllSchools();
        return ApiResponse.getResponse(true,
                "Schools fetched successfully",
                schools
        );
    }
}
