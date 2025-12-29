package com.example.demo.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.PublishMarkRequest;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.TeacherMarksService;

@RestController
@RequestMapping("/teacher/classes")
public class TeacherMarksController {

    private final TeacherMarksService teacherMarksService;

    public TeacherMarksController(TeacherMarksService teacherMarksService) {
        this.teacherMarksService = teacherMarksService;
    }
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/{classId}/students/{studentId}/marks")
    public ResponseEntity<Map<String,Object>> publishMarks(
            @PathVariable UUID classId,
            @PathVariable UUID studentId,
            @RequestBody PublishMarkRequest request) {

        teacherMarksService.publishMarks(classId, studentId, request);

        return ApiResponse.getResponse(true,"Marks published successfully", null);
    }
}

