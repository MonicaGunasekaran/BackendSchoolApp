package com.example.demo.service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.DTO.Student;
import com.example.demo.common.ServiceException;
import com.example.demo.entity.ClassRoom;
import com.example.demo.entity.StudentEntity;
import com.example.demo.entity.User;
import com.example.demo.enumeration.RolesEnum;
import com.example.demo.repository.ClassRoomRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;

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
    public Student createStudent(UUID classId,
                                         Student request) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User teacher = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException("Teacher not found",HttpStatus.NOT_FOUND));

        ClassRoom classRoom = classRoomRepository.findById(classId)
                .orElseThrow(() -> new ServiceException("Class not found",HttpStatus.NOT_FOUND));

        if (!classRoom.getTeacherId().equals(teacher.getId())) {
            throw new ServiceException("You do not own this class",HttpStatus.UNAUTHORIZED);
        }

        if (studentRepository.existsByClassIdAndRollNumber(
                classId,
                request.getRollNumber()
        )) {
            throw new ServiceException("Roll number already exists in this class",HttpStatus.CONFLICT);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ServiceException("Email already exists",HttpStatus.CONFLICT);
        }

        User studentUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RolesEnum.STUDENT)
                .schoolId(teacher.getSchoolId())
                .build();

        User savedUser = userRepository.save(studentUser);

        StudentEntity profile = StudentEntity.builder()
                .name(request.getName())
                .rollNumber(request.getRollNumber())
                .classId(classId)
                .schoolId(teacher.getSchoolId())
                .userId(savedUser.getId())
                .build();

        StudentEntity savedProfile =
                studentRepository.save(profile);

        return Student.builder()
        	    .studentId(savedProfile.getId())
        	    .name(savedProfile.getName())
        	    .rollNumber(savedProfile.getRollNumber())
        	    .classId(savedProfile.getClassId())
        	    .schoolId(savedProfile.getSchoolId())
        	    .userID(savedProfile.getUserId())
        	    .build();

    }
    public List<Student> getStudents(UUID classId) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User teacher = userRepository.findByEmail(email)
            .orElseThrow(() -> new ServiceException(
                "Teacher not found", HttpStatus.UNAUTHORIZED));

        if (teacher.getRole() != RolesEnum.TEACHER) {
            throw new ServiceException(
               "Only teachers can view students", HttpStatus.FORBIDDEN);
        }

        ClassRoom classRoom = classRoomRepository.findById(classId)
            .orElseThrow(() -> new ServiceException(
            		 "Class not found",HttpStatus.NOT_FOUND));

        if (!classRoom.getTeacherId().equals(teacher.getId())) {
            throw new ServiceException(
                 "You do not own this class",HttpStatus.FORBIDDEN);
        }

        
        List<StudentEntity> students =
                studentRepository.findByClassId(classId);

        return students.stream()
                .map(s -> new Student(
                        s.getId(),
                        s.getName(),
                        s.getRollNumber(),
                        s.getClassId()
                ))
                .collect(Collectors.toList());
    }

}
