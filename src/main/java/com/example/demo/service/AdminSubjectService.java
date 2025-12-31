package com.example.demo.service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.demo.DTO.CreateSubject;
import com.example.demo.DTO.SubjectResponse;
import com.example.demo.common.ServiceException;
import com.example.demo.entity.Subject;
import com.example.demo.repository.SubjectRepository;
@Service
public class AdminSubjectService {
    private final SubjectRepository subjectRepository;
    public AdminSubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }
    public Subject createSubject(CreateSubject request) {
        if (subjectRepository.findAll()
                .stream()
                .anyMatch(s -> s.getName().equalsIgnoreCase(request.getName()))) {
            throw new ServiceException(
            		"Subject already exists",
                HttpStatus.BAD_REQUEST
                
            );
        }
        Subject subject = Subject.builder()
                .name(request.getName())
                .build();

        return subjectRepository.save(subject);
    }
    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAll()
            .stream()
            .map(s -> new SubjectResponse(s.getId(), s.getName()))
            .collect(Collectors.toList());
    }

}
