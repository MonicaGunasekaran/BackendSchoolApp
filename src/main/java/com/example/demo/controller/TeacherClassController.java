package com.example.demo.controller;

import com.example.demo.entity.ClassRoom;
import com.example.demo.DTO.CreateClass;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.TeacherClassService;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacher/classes")
public class TeacherClassController {

    private final TeacherClassService teacherClassService;

    public TeacherClassController(TeacherClassService teacherClassService) {
        this.teacherClassService = teacherClassService;
    }

    @PostMapping
    public ApiResponse<ClassRoom> createClass(
            @RequestBody CreateClass request) {

        ClassRoom classRoom =
            teacherClassService.createClass(request);

        return ApiResponse.success(
            "Class created successfully",
            classRoom
        );
    }
    
    @GetMapping
    public ApiResponse<List<ClassRoom>> getMyClasses() {

        List<ClassRoom> classes =
                teacherClassService.getAllClasses();
        return ApiResponse.success(
                "Classes fetched successfully",
                classes
        );
    }}
