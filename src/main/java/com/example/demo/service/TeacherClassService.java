package com.example.demo.service;

import com.example.demo.entity.ClassRoom;
import com.example.demo.entity.Curriculum;
import com.example.demo.entity.User;
import com.example.demo.repository.ClassRoomRepository;
import com.example.demo.repository.CurriculumRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.DTO.CreateClass;
import com.example.demo.enumeration.RolesEnum;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TeacherClassService {

    private final ClassRoomRepository classRoomRepository;
    private final UserRepository userRepository;
    private final CurriculumRepository curriculumRepository;

    // ✅ FIXED CONSTRUCTOR
    public TeacherClassService(ClassRoomRepository classRoomRepository,
                               UserRepository userRepository,
                               CurriculumRepository curriculumRepository) {
        this.classRoomRepository = classRoomRepository;
        this.userRepository = userRepository;
        this.curriculumRepository = curriculumRepository;
    }

    // ✅ CREATE CLASS
    public ClassRoom createClass(CreateClass request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User teacher = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Teacher not found"
            ));

        if (teacher.getRole() != RolesEnum.TEACHER) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Only teachers can create classes"
            );
        }

        // ✅ FIND CURRICULUM (MANDATORY)
        Curriculum curriculum = curriculumRepository
            .findByGradeAndSection(request.getGrade(), request.getSection())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "No curriculum defined for this grade and section"
            ));

        // ✅ PREVENT DUPLICATE CLASS
        if (classRoomRepository.existsBySchoolIdAndGradeAndSection(
                teacher.getSchoolId(),
                request.getGrade(),
                request.getSection()
        )) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Class already exists in this school"
            );
        }

        // ✅ CREATE CLASS WITH curriculumId
        ClassRoom classRoom = ClassRoom.builder()
                .grade(request.getGrade())
                .section(request.getSection())
                .schoolId(teacher.getSchoolId())
                .teacherId(teacher.getId())
                .curriculumId(curriculum.getId()) // 🔥 IMPORTANT
                .build();

        return classRoomRepository.save(classRoom);
    }

    // ✅ LIST CLASSES
    public List<ClassRoom> getAllClasses() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User teacher = userRepository.findByEmail(email)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Teacher not found"
                )
            );

        if (teacher.getRole() != RolesEnum.TEACHER) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Only teachers can view classes"
            );
        }

        return classRoomRepository.findByTeacherId(teacher.getId());
    }
}
