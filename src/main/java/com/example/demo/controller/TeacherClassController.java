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
import com.example.demo.DTO.CreateClass;
import com.example.demo.common.ApiResponse;
import com.example.demo.entity.ClassRoom;
import com.example.demo.service.TeacherClassService;
@RestController
@RequestMapping("/teacher/classes")
public class TeacherClassController {
    private final TeacherClassService teacherClassService;
    public TeacherClassController(TeacherClassService teacherClassService) {
        this.teacherClassService = teacherClassService;
    }
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Map<String,Object>> createClass(
            @RequestBody CreateClass request) {
        ClassRoom classRoom =
            teacherClassService.createClass(request);
        return ApiResponse.getResponse(true,
            "Class created successfully",
            classRoom
        );
    }
    @GetMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Map<String,Object>> getMyClasses() {
        List<ClassRoom> classes =
                teacherClassService.getAllClasses();
        return ApiResponse.getResponse(true,
                "Classes fetched successfully",
                classes
        );
    }}
