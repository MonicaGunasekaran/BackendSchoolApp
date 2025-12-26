package com.example.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.StudentResultResponse;
import com.example.demo.DTO.SubjectResult;
import com.example.demo.common.ServiceException;
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

        // Logged-in student email from JWT
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException(
                         "User not found",HttpStatus.UNAUTHORIZED));

        if (user.getRole() != RolesEnum.STUDENT) {
            throw new ServiceException(
            		"Only students can view results", HttpStatus.FORBIDDEN);
        }

        //Student profile
        StudentEntity student = studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new 
                		
                		ServiceException(
                       "Student profile not found",  HttpStatus.NOT_FOUND));

        //Fetch marks
        List<MarkEntity> marks =
                markRepository.findByStudentId(student.getId());
        String status = "PASS";

        for (MarkEntity mark : marks) {
            if (mark.getMarks() < 35) {
                status = "FAIL";
                break;
            }
        }
        //Map subject names
        List<SubjectResult> subjectResults = marks.stream()
                .map(mark -> {
                    Subject subject = subjectRepository.findById(
                            mark.getSubjectId())
                            .orElseThrow(() -> new ServiceException(
                            		"Subject missing", HttpStatus.INTERNAL_SERVER_ERROR
                                    ));

                    return new SubjectResult(
                            subject.getName(),
                            mark.getMarks()
                    );
                })
                .toList();

        //Response
        return new StudentResultResponse(
                student.getName(),
                student.getRollNumber(),
                student.getClassId(),
                subjectResults, status
        );
    }
}
