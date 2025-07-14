package com.quicksearch.quicksearch.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quicksearch.quicksearch.document.CourseDocument;
import com.quicksearch.quicksearch.repository.CourseRepository;

@Service
public class IngestionService {

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    public IngestionService(CourseRepository courseRepository, ObjectMapper objectMapper) {
        this.courseRepository = courseRepository;
        this.objectMapper = objectMapper;
    }

    public void ingestCourses() {
        try {
            InputStream inputStream = new ClassPathResource("sample-courses.json").getInputStream();
            List<CourseDocument> courses = objectMapper.readValue(inputStream,
                    new TypeReference<List<CourseDocument>>() {
                    });
            courseRepository.saveAll(courses);
        } catch (Exception e) {
            System.err.println("Failed to ingest cources: " + e.getMessage());

        }
    }
}
