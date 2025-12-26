package com.example.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.CreateClass;
import com.example.demo.common.ServiceException;
import com.example.demo.entity.ClassRoom;
import com.example.demo.entity.Curriculum;
import com.example.demo.entity.User;
import com.example.demo.enumeration.RolesEnum;
import com.example.demo.repository.ClassRoomRepository;
import com.example.demo.repository.CurriculumRepository;
import com.example.demo.repository.UserRepository;

@Service
public class TeacherClassService {

    private final ClassRoomRepository classRoomRepository;
    private final UserRepository userRepository;
    private final CurriculumRepository curriculumRepository;

    //FIXED CONSTRUCTOR
    public TeacherClassService(ClassRoomRepository classRoomRepository,
                               UserRepository userRepository,
                               CurriculumRepository curriculumRepository) {
        this.classRoomRepository = classRoomRepository;
        this.userRepository = userRepository;
        this.curriculumRepository = curriculumRepository;
    }

    // CREATE CLASS
    public ClassRoom createClass(CreateClass request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User teacher = userRepository.findByEmail(email)
            .orElseThrow(() -> new ServiceException(
            		"Teacher not found",HttpStatus.UNAUTHORIZED
                
            ));

        if (teacher.getRole() != RolesEnum.TEACHER) {
            throw new ServiceException(
                
                "Only teachers can create classes",HttpStatus.FORBIDDEN
            );
        }

        // FIND CURRICULUM (MANDATORY)
        Curriculum curriculum = curriculumRepository
            .findByGradeAndSection(request.getGrade(), request.getSection())
            .orElseThrow(() -> new ServiceException(
                
                "No curriculum defined for this grade and section",HttpStatus.BAD_REQUEST
            ));

        // PREVENT DUPLICATE CLASS
        if (classRoomRepository.existsBySchoolIdAndGradeAndSection(
                teacher.getSchoolId(),
                request.getGrade(),
                request.getSection()
        )) {
            throw new ServiceException(
               
                "Class already exists in this school", HttpStatus.BAD_REQUEST
            );
        }

        // CREATE CLASS WITH curriculumId
        ClassRoom classRoom = ClassRoom.builder()
                .grade(request.getGrade())
                .section(request.getSection())
                .schoolId(teacher.getSchoolId())
                .teacherId(teacher.getId())
                .curriculumId(curriculum.getId()) // 🔥 IMPORTANT
                .build();

        return classRoomRepository.save(classRoom);
    }

    // LIST CLASSES
    public List<ClassRoom> getAllClasses() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User teacher = userRepository.findByEmail(email)
            .orElseThrow(() ->
                new ServiceException(
                		   "Teacher not found",
                    HttpStatus.UNAUTHORIZED
                 
                )
            );

        if (teacher.getRole() != RolesEnum.TEACHER) {
            throw new ServiceException(
               
                "Only teachers can view classes", HttpStatus.FORBIDDEN
            );
        }

        return classRoomRepository.findByTeacherId(teacher.getId());
    }
}
