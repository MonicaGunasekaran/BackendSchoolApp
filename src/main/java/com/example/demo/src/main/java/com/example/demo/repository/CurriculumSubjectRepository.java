package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.CurriculumSubject;

import java.util.List;
import java.util.UUID;

public interface CurriculumSubjectRepository
        extends JpaRepository<CurriculumSubject, UUID> {

    List<CurriculumSubject> findByCurriculumId(UUID curriculumId);

    boolean existsByCurriculumIdAndSubjectId(UUID curriculumId, UUID subjectId);
}
