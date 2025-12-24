package com.example.demo.service;
import com.example.demo.DTO.SchoolResponse;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.SchoolEntity;
import com.example.demo.repository.SchoolRepository;
import org.springframework.stereotype.Service;
import com.example.demo.DTO.*;
@Service
public class AdminSchoolService {

    private final SchoolRepository schoolRepository;

    public AdminSchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    public SchoolResponse createSchool(CreateSchool request) {

        if (schoolRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"School Already exists");
        }

        SchoolEntity school = SchoolEntity.builder()
                .name(request.getName())
                .address(request.getAddress())
                .build();

        SchoolEntity saved = schoolRepository.save(school);

        return new SchoolResponse(
                saved.getId(),
                saved.getName(),
                saved.getAddress()
        );
    }
    public List<SchoolResponse> getAllSchools() {

        List<SchoolEntity> schools = schoolRepository.findAll();

        return schools.stream()
                .map(school -> new SchoolResponse(
                        school.getId(),
                        school.getName(),
                        school.getAddress()
                ))
                .collect(Collectors.toList());
    }
    
}
