package com.example.demo.controller;

import com.example.demo.DTO.*;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.TeacherStudentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    public ResponseEntity<Map<String,Object>> createStudent(
            @PathVariable UUID classId,
            @RequestBody Student request) {

        Student response =
                teacherStudentService.createStudent(classId, request);

        return ApiResponse.getResponse(
                true,
                "Student created successfully",
                response
        );
    }


    // ✅ LIST STUDENTS
    @GetMapping
    public ResponseEntity<Map<String,Object>> getStudents(
            @PathVariable UUID classId) {

        List<Student> students =
                teacherStudentService.getStudents(classId);

        return ApiResponse.getResponse(
        		true,
                "Students fetched successfully",
                students
        );
    }
}
