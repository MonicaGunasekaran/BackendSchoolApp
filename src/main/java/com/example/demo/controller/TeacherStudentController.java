package com.example.demo.controller;

import com.example.demo.DTO.*;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.TeacherStudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/teacher/classes/{classId}/students")
public class TeacherStudentController {

    private final TeacherStudentService teacherStudentService;

    public TeacherStudentController(TeacherStudentService teacherStudentService) {
        this.teacherStudentService = teacherStudentService;
    }

    // ✅ CREATE STUDENT
    @PostMapping
    public ApiResponse<StudentResponse> createStudent(
            @PathVariable UUID classId,
            @RequestBody CreateStudent request) {

        StudentResponse response =
                teacherStudentService.createStudent(classId, request);

        return ApiResponse.success(
                "Student created successfully",
                response
        );
    }

    // ✅ LIST STUDENTS
    @GetMapping
    public ApiResponse<List<StudentResponse>> getStudents(
            @PathVariable UUID classId) {

        List<StudentResponse> students =
                teacherStudentService.getStudents(classId);

        return ApiResponse.success(
                "Students fetched successfully",
                students
        );
    }
}
