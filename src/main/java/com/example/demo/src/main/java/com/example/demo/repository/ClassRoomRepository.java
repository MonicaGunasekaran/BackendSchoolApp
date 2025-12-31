package com.example.demo.repository;

import com.example.demo.entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, UUID> {

    boolean existsBySchoolIdAndGradeAndSection(
            UUID schoolId,
            Integer grade,
            String section
    );
    List<ClassRoom> findByTeacherId(UUID teacherId);
}
