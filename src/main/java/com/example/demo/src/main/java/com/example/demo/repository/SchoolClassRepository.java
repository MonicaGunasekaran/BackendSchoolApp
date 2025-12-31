package com.example.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ClassRoom;

public interface SchoolClassRepository extends JpaRepository<ClassRoom,UUID> {
	List<ClassRoom> findBySchoolId(UUID schoolId);
}
