package com.example.demo.controller;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.common.ApiResponse;
import com.example.demo.service.SchoolClassService;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/admin/schools/{schoolId}")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
public class SchoolClassController {
    private final SchoolClassService schoolClassService;
    @GetMapping("/classes")
    public ResponseEntity<Map<String, Object>> getClassesBySchool(
            @PathVariable UUID schoolId) {
        return ApiResponse.getResponse(
                true,
                "Classes fetched successfully",
                schoolClassService.getClassesBySchool(schoolId)
        );
    }
}
