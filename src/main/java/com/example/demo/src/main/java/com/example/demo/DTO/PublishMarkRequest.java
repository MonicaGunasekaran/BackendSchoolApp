package com.example.demo.DTO;

import lombok.Data;
import java.util.List;

@Data
public class PublishMarkRequest {
    private List<SubjectMark> subjects;
}
