package com.example.demo.service;
import com.example.demo.DTO.School;
import com.example.demo.common.ServiceException;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.example.demo.entity.SchoolEntity;
import com.example.demo.repository.SchoolRepository;
import org.springframework.stereotype.Service;
@Service
public class AdminSchoolService {
    private final SchoolRepository schoolRepository;
    public AdminSchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }
    public School createSchool(School request) {

        if (schoolRepository.existsByName(request.getName())) {
        	 throw new ServiceException("School already exists", HttpStatus.OK);
        }

        SchoolEntity school = SchoolEntity.builder()
                .name(request.getName())
                .address(request.getAddress())
                .build();

        SchoolEntity saved = schoolRepository.save(school);

        return new School(
                saved.getAddress(),
                saved.getName(),
                saved.getId()
        );
    }
    public List<School> getAllSchools() {
        List<SchoolEntity> schools = schoolRepository.findAll();
        return schools.stream()
                .map(school -> new School(
                        school.getAddress(),
                        school.getName(),
                        school.getId()
                ))
                .collect(Collectors.toList());
    }   
}
