package com.example.demo.service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.demo.DTO.ClassRoomResponse;
import com.example.demo.repository.SchoolClassRepository;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class SchoolClassService {
    private final SchoolClassRepository schoolClassRepository;
    public List<ClassRoomResponse> getClassesBySchool(UUID schoolId) {
        return schoolClassRepository.findBySchoolId(schoolId)
                .stream()
                .map(c -> new ClassRoomResponse(
                        c.getId(),
                        c.getGrade(),
                        c.getSection()
                ))
                .collect(Collectors.toList());
    }
}

