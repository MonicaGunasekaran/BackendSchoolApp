package com.example.demo.service;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.demo.DTO.MarkResponse;
import com.example.demo.DTO.PublishMarkRequest;
import com.example.demo.DTO.SubjectMark;
import com.example.demo.common.ServiceException;
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
        subjectRepository.findById(subjectId)
            .orElseThrow(() -> new ServiceException(
            		"Subject not found", HttpStatus.NOT_FOUND));
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
            throw new ServiceException(
            		 "No subjects provided",HttpStatus.BAD_REQUEST);
        }
        //  Logged-in teacher
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User teacher = userRepository.findByEmail(email)
            .orElseThrow(() -> new ServiceException(
                 "Teacher not found",HttpStatus.UNAUTHORIZED));
        if (teacher.getRole() != RolesEnum.TEACHER) {
            throw new ServiceException(
            		"Only teachers can publish marks",  HttpStatus.FORBIDDEN);
        }
        //  Verify class
        ClassRoom classRoom = classRoomRepository.findById(classId)
            .orElseThrow(() -> new ServiceException(
                "Class not found", HttpStatus.NOT_FOUND));
        if (!classRoom.getTeacherId().equals(teacher.getId())) {
            throw new ServiceException(
            		"You do not own this class", HttpStatus.FORBIDDEN);
        }
        // Verify student
        StudentEntity student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ServiceException(
            		"Student not found", HttpStatus.NOT_FOUND ));
        if (!student.getClassId().equals(classId)) {
            throw new ServiceException(
               "Student does not belong to this class", HttpStatus.BAD_REQUEST);
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
            .orElseThrow(() -> new ServiceException(
            		"Teacher not found", HttpStatus.UNAUTHORIZED));
        if (teacher.getRole() != RolesEnum.TEACHER) {
            throw new ServiceException(
            		"Only teachers can view marks", HttpStatus.FORBIDDEN);
        }
        // 2️⃣ Verify class
        ClassRoom classRoom = classRoomRepository.findById(classId)
            .orElseThrow(() -> new ServiceException(
                "Class not found", HttpStatus.NOT_FOUND));
        if (!classRoom.getTeacherId().equals(teacher.getId())) {
            throw new ServiceException(
            		"You do not own this class", HttpStatus.FORBIDDEN);
        }
        // 3️⃣ Verify student
        StudentEntity student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ServiceException(
            		 "Student not found",HttpStatus.NOT_FOUND));
        if (!student.getClassId().equals(classId)) {
            throw new ServiceException(
            		 "Student does not belong to this class", HttpStatus.BAD_REQUEST);
        }
        // 4️⃣ Fetch marks
        List<MarkEntity> marks =
                markRepository.findByStudentId(studentId);
        // 5️⃣ Map to response DTO
        return marks.stream()
                .map(mark -> {
                    Subject subject = subjectRepository.findById(
                            mark.getSubjectId()
                    ).orElseThrow(() -> new ServiceException(
                            
                            "Subject missing for mark",HttpStatus.INTERNAL_SERVER_ERROR
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
 