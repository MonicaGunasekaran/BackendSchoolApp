package com.example.demo.controller;

import com.example.demo.DTO.PublishMarkRequest;
import com.example.demo.DTO.PublishMarks;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.TeacherMarksService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/teacher/classes")
public class TeacherMarksController {

    private final TeacherMarksService teacherMarksService;

    public TeacherMarksController(TeacherMarksService teacherMarksService) {
        this.teacherMarksService = teacherMarksService;
    }
    @PostMapping("/{classId}/students/{studentId}/marks")
    public ApiResponse<Void> publishMarks(
            @PathVariable UUID classId,
            @PathVariable UUID studentId,
            @RequestBody PublishMarkRequest request) {   // ✅ CORRECT

        teacherMarksService.publishMarks(classId, studentId, request);

        return ApiResponse.success("Marks published successfully", null);
    }
}

