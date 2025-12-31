package com.example.demo.repository;

import com.example.demo.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepository
        extends JpaRepository<StudentEntity, UUID> {

    boolean existsByClassIdAndRollNumber(UUID classId, Integer rollNumber);
    List<StudentEntity> findByClassId(UUID classId);
    Optional<StudentEntity> findByUserId(UUID userId); 

}
