package com.quicksearch.quicksearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import jakarta.annotation.PostConstruct;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.quicksearch.quicksearch.document.CourseDocument;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class IngestionService {

    private final ElasticsearchClient client;
    private final ObjectMapper objectMapper;

    public IngestionService(ElasticsearchClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.objectMapper.findAndRegisterModules();
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @PostConstruct
    public void ingestCourses() {
        try {
            InputStream inputStream = new ClassPathResource("sample-courses.json").getInputStream();
            List<CourseDocument> courses = objectMapper.readValue(inputStream, new TypeReference<>() {
            });

            if (courses.isEmpty()) {
                System.out.println("No courses found in sample-courses.json");
                return;
            }

            List<BulkOperation> operations = new ArrayList<>();

            for (CourseDocument course : courses) {
                operations.add(BulkOperation.of(op -> op
                        .index(idx -> idx
                                .index("courses")
                                .id(course.getId())
                                .document(course))));
            }

            BulkRequest request = new BulkRequest.Builder()
                    .operations(operations)
                    .build();

            client.bulk(request);
            System.out.println("Successfully ingested " + courses.size() + " courses.");

        } catch (Exception e) {
            System.err.println("Failed to ingest courses: " + e.getMessage());

            e.printStackTrace();
        }
    }
}
