package com.example.demo.controller;

import com.example.demo.DTO.*;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.*;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/schools")
public class AdminSchoolController {

    private final AdminSchoolService adminSchoolService;

    public AdminSchoolController(AdminSchoolService adminSchoolService) {
        this.adminSchoolService = adminSchoolService;
    }

    @PostMapping
    public ApiResponse<SchoolResponse> createSchool(@RequestBody CreateSchool request) {
        SchoolResponse response=adminSchoolService.createSchool(request);
        return ApiResponse.success("School created successfully", response);
    }
    @GetMapping
    public ApiResponse<List<SchoolResponse>> getAllSchools() {

        List<SchoolResponse> schools =
                adminSchoolService.getAllSchools();
        return ApiResponse.success(
                "Schools fetched successfully",
                schools
        );
    }
}
