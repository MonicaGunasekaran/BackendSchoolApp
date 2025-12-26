package com.example.demo.controller;

import com.example.demo.DTO.PublishMarkRequest;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.TeacherMarksService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/teacher/classes")
public class TeacherMarksController {

    private final TeacherMarksService teacherMarksService;

    public TeacherMarksController(TeacherMarksService teacherMarksService) {
        this.teacherMarksService = teacherMarksService;
    }
    @PostMapping("/{classId}/students/{studentId}/marks")
    public ResponseEntity<Map<String,Object>> publishMarks(
            @PathVariable UUID classId,
            @PathVariable UUID studentId,
            @RequestBody PublishMarkRequest request) {

        teacherMarksService.publishMarks(classId, studentId, request);

        return ApiResponse.getResponse(true,"Marks published successfully", null);
    }
}

