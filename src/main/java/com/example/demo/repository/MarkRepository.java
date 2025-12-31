package com.example.demo.repository;

import com.example.demo.entity.MarkEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MarkRepository extends JpaRepository<MarkEntity, UUID> {

    Optional<MarkEntity> findByStudentIdAndSubjectId(UUID studentId, UUID subjectId);
    List<MarkEntity> findByStudentId(UUID studentId);
}
