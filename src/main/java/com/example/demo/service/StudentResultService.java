package com.example.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.DTO.StudentResultResponse;
import com.example.demo.DTO.SubjectResult;
import com.example.demo.entity.MarkEntity;
import com.example.demo.entity.StudentEntity;
import com.example.demo.entity.Subject;
import com.example.demo.entity.User;
import com.example.demo.enumeration.RolesEnum;
import com.example.demo.repository.MarkRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.repository.UserRepository;

@Service
public class StudentResultService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final MarkRepository markRepository;
    private final SubjectRepository subjectRepository;

    public StudentResultService(
            UserRepository userRepository,
            StudentRepository studentRepository,
            MarkRepository markRepository,
            SubjectRepository subjectRepository) {

        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.markRepository = markRepository;
        this.subjectRepository = subjectRepository;
    }

    public StudentResultResponse getStudentResults() {

        // 1️⃣ Logged-in student email from JWT
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User not found"));

        if (user.getRole() != RolesEnum.STUDENT) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only students can view results");
        }

        // 2️⃣ Student profile
        StudentEntity student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student profile not found"));

        // 3️⃣ Fetch marks
        List<MarkEntity> marks =
                markRepository.findByStudentId(student.getId());
        String status = "PASS";

        for (MarkEntity mark : marks) {
            if (mark.getMarks() < 35) {
                status = "FAIL";
                break;
            }
        }
        // 4️⃣ Map subject names
        List<SubjectResult> subjectResults = marks.stream()
                .map(mark -> {
                    Subject subject = subjectRepository.findById(
                            mark.getSubjectId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.INTERNAL_SERVER_ERROR,
                                    "Subject missing"));

                    return new SubjectResult(
                            subject.getName(),
                            mark.getMarks()
                    );
                })
                .toList();

        // 5️⃣ Response
        return new StudentResultResponse(
                student.getName(),
                student.getRollNumber(),
                student.getClassId(),
                subjectResults, status
        );
    }
}
