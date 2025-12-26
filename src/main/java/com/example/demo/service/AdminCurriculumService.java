package com.example.demo.service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.demo.DTO.CreateCurriculum;
import com.example.demo.DTO.CurriculumResponse;
import com.example.demo.common.ServiceException;
import com.example.demo.entity.Curriculum;
import com.example.demo.repository.CurriculumRepository;

@Service
public class AdminCurriculumService {

    private final CurriculumRepository curriculumRepository;

    public AdminCurriculumService(CurriculumRepository curriculumRepository) {
        this.curriculumRepository = curriculumRepository;
    }

    public Curriculum createCurriculum(CreateCurriculum request) {

        if (curriculumRepository.findByGradeAndSection(
                request.getGrade(), request.getSection()).isPresent()) {
            throw new ServiceException(
            		"Curriculum already exists for this grade and section",
                HttpStatus.BAD_REQUEST
                
            );
        }

        Curriculum curriculum = Curriculum.builder()
                .grade(request.getGrade())
                .section(request.getSection())
                .build();

        return curriculumRepository.save(curriculum);
    }
    public List<CurriculumResponse> getAllCurriculums() {

        List<Curriculum> curriculums = curriculumRepository.findAll();

        return curriculums.stream()
                .map(c -> new CurriculumResponse(
                        c.getId(),
                        c.getGrade(),
                        c.getSection()
                ))
                .collect(Collectors.toList());
    }
}
