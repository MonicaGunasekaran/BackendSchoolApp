package com.example.demo.service;

import com.example.demo.DTO.*;
import com.example.demo.enumeration.RolesEnum;
import com.example.demo.entity.SchoolEntity;
import com.example.demo.entity.User;
import com.example.demo.repository.SchoolRepository;
import com.example.demo.repository.UserRepository;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminTeacherService {

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminTeacherService(UserRepository userRepository,
                               SchoolRepository schoolRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public TeacherResponse createTeacher(UUID schoolId,
                                         CreateTeacher request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Email already exists"
            );
        }

        SchoolEntity school = schoolRepository.findById(schoolId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "School not found"
            ));

        User teacher = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(RolesEnum.TEACHER)
            .schoolId(school.getId())
            .build();

        User saved = userRepository.save(teacher);

        return new TeacherResponse(
            saved.getId(),
            saved.getEmail(),
            saved.getSchoolId()
        );
    }
}
