package com.example.demo.controller;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.DTO.Student;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.TeacherStudentService;
@RestController
@RequestMapping("/teacher/classes/{classId}/students")
public class TeacherStudentController {
    private final TeacherStudentService teacherStudentService;
    public TeacherStudentController(TeacherStudentService teacherStudentService) {
        this.teacherStudentService = teacherStudentService;
    }
    @PreAuthorize("hasRole('TEACHER')")
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
    @PreAuthorize("hasRole('TEACHER')")
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
