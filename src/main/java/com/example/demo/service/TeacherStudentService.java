package com.example.demo.service;
import com.example.demo.entity.ClassRoom;
import com.example.demo.entity.StudentEntity;
import com.example.demo.enumeration.RolesEnum;
import com.example.demo.entity.User;
import com.example.demo.repository.ClassRoomRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.DTO.*;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeacherStudentService {

    private final ClassRoomRepository classRoomRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;

    public TeacherStudentService(ClassRoomRepository classRoomRepository,
                                 UserRepository userRepository,
                                 StudentRepository studentRepository,
                                 
                                 PasswordEncoder passwordEncoder) {
        this.classRoomRepository = classRoomRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public StudentResponse createStudent(UUID classId,
                                         CreateStudent request) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User teacher = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Teacher not found"));

        // 2️⃣ Verify class ownership
        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Class not found"));

        if (!classRoom.getTeacherId().equals(teacher.getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"You do not own this class");
        }

        // 3️⃣ Check roll number uniqueness
        if (studentRepository.existsByClassIdAndRollNumber(
                classId,
                request.getRollNumber()
        )) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Roll number already exists in this class");
        }

        // 4️⃣ Check email uniqueness
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");
        }

        // 5️⃣ Create USER (login identity)
        User studentUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RolesEnum.STUDENT)
                .schoolId(teacher.getSchoolId())
                .build();

        User savedUser = userRepository.save(studentUser);

        // 6️⃣ Create STUDENT PROFILE
        StudentEntity profile = StudentEntity.builder()
                .name(request.getName())
                .rollNumber(request.getRollNumber())
                .classId(classId)
                .schoolId(teacher.getSchoolId())
                .userId(savedUser.getId())
                .build();

        StudentEntity savedProfile =
                studentRepository.save(profile);

        return new StudentResponse(
                savedProfile.getId(),
                savedProfile.getName(),
                savedProfile.getRollNumber(),
                savedProfile.getClassId()
        );
    }
    public List<StudentResponse> getStudents(UUID classId) {

        // 1️⃣ Logged-in teacher
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User teacher = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Teacher not found"));

        if (teacher.getRole() != RolesEnum.TEACHER) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "Only teachers can view students");
        }

        // 2️⃣ Class ownership check
        ClassRoom classRoom = classRoomRepository.findById(classId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Class not found"));

        if (!classRoom.getTeacherId().equals(teacher.getId())) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "You do not own this class");
        }
        // 3️⃣ Fetch students
        List<StudentEntity> students =
                studentRepository.findByClassId(classId);

        return students.stream()
                .map(s -> new StudentResponse(
                        s.getId(),
                        s.getName(),
                        s.getRollNumber(),
                        s.getClassId()
                ))
                .collect(Collectors.toList());
    }
}
