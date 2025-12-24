package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.DTO.MarkResponse;
import com.example.demo.DTO.PublishMarkRequest;
import com.example.demo.DTO.SubjectMark;
import com.example.demo.entity.ClassRoom;
import com.example.demo.entity.MarkEntity;
import com.example.demo.entity.StudentEntity;
import com.example.demo.entity.Subject;
import com.example.demo.entity.User;
import com.example.demo.enumeration.RolesEnum;
import com.example.demo.repository.ClassRoomRepository;
import com.example.demo.repository.MarkRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.repository.UserRepository;

@Service
public class TeacherMarksService {

    private final MarkRepository markRepository;
    private final StudentRepository studentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public TeacherMarksService(
            MarkRepository markRepository,
            StudentRepository studentRepository,
            ClassRoomRepository classRoomRepository,
            UserRepository userRepository,
            SubjectRepository subjectRepository) {

        this.markRepository = markRepository;
        this.studentRepository = studentRepository;
        this.classRoomRepository = classRoomRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }
    private void publishSingleMark(
            User teacher,
            ClassRoom classRoom,
            StudentEntity student,
            UUID subjectId,
            Integer marks) {

        // Verify subject exists
        subjectRepository.findById(subjectId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Subject not found"));

        markRepository.findByStudentIdAndSubjectId(student.getId(), subjectId)
            .ifPresentOrElse(
                existing -> {
                    existing.setMarks(marks);
                    markRepository.save(existing);
                },
                () -> {
                    MarkEntity mark = MarkEntity.builder()
                            .studentId(student.getId())
                            .classId(classRoom.getId())
                            .subjectId(subjectId)
                            .marks(marks)
                            .build();
                    markRepository.save(mark);
                }
            );
    }

    public void publishMarks(
            UUID classId,
            UUID studentId,
            PublishMarkRequest request) {

        if (request.getSubjects() == null || request.getSubjects().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "No subjects provided");
        }

        // 1️⃣ Logged-in teacher
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User teacher = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Teacher not found"));

        if (teacher.getRole() != RolesEnum.TEACHER) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "Only teachers can publish marks");
        }

        // 2️⃣ Verify class
        ClassRoom classRoom = classRoomRepository.findById(classId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Class not found"));

        if (!classRoom.getTeacherId().equals(teacher.getId())) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "You do not own this class");
        }

        // 3️⃣ Verify student
        StudentEntity student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Student not found"));

        if (!student.getClassId().equals(classId)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Student does not belong to this class");
        }

        // 4️⃣ Loop subjects
        for (SubjectMark sm : request.getSubjects()) {
            publishSingleMark(
                teacher,
                classRoom,
                student,
                sm.getSubjectId(),
                sm.getMarks()
            );
        }
    }
    public List<MarkResponse> getStudentMarks(UUID classId, UUID studentId) {

        // 1️⃣ Logged-in teacher
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User teacher = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Teacher not found"));

        if (teacher.getRole() != RolesEnum.TEACHER) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "Only teachers can view marks");
        }

        // 2️⃣ Verify class
        ClassRoom classRoom = classRoomRepository.findById(classId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Class not found"));

        if (!classRoom.getTeacherId().equals(teacher.getId())) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "You do not own this class");
        }

        // 3️⃣ Verify student
        StudentEntity student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Student not found"));

        if (!student.getClassId().equals(classId)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Student does not belong to this class");
        }

        // 4️⃣ Fetch marks
        List<MarkEntity> marks =
                markRepository.findByStudentId(studentId);

        // 5️⃣ Map to response DTO
        return marks.stream()
                .map(mark -> {
                    Subject subject = subjectRepository.findById(
                            mark.getSubjectId()
                    ).orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Subject missing for mark"
                    ));

                    return new MarkResponse(
                            subject.getId(),
                            subject.getName(),
                            mark.getMarks()
                    );
                })
                .toList();
    }
}
  