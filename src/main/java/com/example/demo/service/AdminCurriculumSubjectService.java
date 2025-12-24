package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.DTO.AddSubjectsToCurriculum;
import com.example.demo.DTO.CurriculumSubjectResponse;
import com.example.demo.entity.Curriculum;
import com.example.demo.entity.CurriculumSubject;
import com.example.demo.entity.Subject;
import com.example.demo.repository.CurriculumRepository;
import com.example.demo.repository.CurriculumSubjectRepository;
import com.example.demo.repository.SubjectRepository;

@Service
public class AdminCurriculumSubjectService {

    private final CurriculumRepository curriculumRepository;
    private final SubjectRepository subjectRepository;
    private final CurriculumSubjectRepository curriculumSubjectRepository;

    public AdminCurriculumSubjectService(CurriculumRepository curriculumRepository,
                                         SubjectRepository subjectRepository,
                                         CurriculumSubjectRepository curriculumSubjectRepository) {
        this.curriculumRepository = curriculumRepository;
        this.subjectRepository = subjectRepository;
        this.curriculumSubjectRepository = curriculumSubjectRepository;
    }

    public void addSubjects(UUID curriculumId, AddSubjectsToCurriculum request) {

        Curriculum curriculum = curriculumRepository.findById(curriculumId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Curriculum not found"));

        for (UUID subjectId : request.getSubjectIds()) {

            Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Subject not found"));

            if (curriculumSubjectRepository
                    .existsByCurriculumIdAndSubjectId(curriculumId, subjectId)) {
                continue; 
            }

            CurriculumSubject mapping = CurriculumSubject.builder()
                    .curriculum(curriculum)
                    .subject(subject)
                    .build();

            curriculumSubjectRepository.save(mapping);
        }
    }
    public List<CurriculumSubjectResponse> getSubjectsByCurriculum(
            UUID curriculumId) {

        // 1️⃣ Validate curriculum
        if (!curriculumRepository.existsById(curriculumId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Curriculum not found");
        }

        // 2️⃣ Fetch mappings
        List<CurriculumSubject> mappings =
                curriculumSubjectRepository
                        .findByCurriculumId(curriculumId);

        // 3️⃣ Map to response
        return mappings.stream()
                .map(cs -> new CurriculumSubjectResponse(
                        cs.getSubject().getId(),
                        cs.getSubject().getName(),
                        cs.getSubject().getTotalMarks()
                ))
                .toList();
    }
}
