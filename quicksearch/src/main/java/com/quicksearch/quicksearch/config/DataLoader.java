package com.quicksearch.quicksearch.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.quicksearch.quicksearch.service.IngestionService;

@Component
public class DataLoader implements ApplicationRunner {
    private final IngestionService courseIngestionService;

    public DataLoader(IngestionService courseIngestionService) {
        this.courseIngestionService = courseIngestionService;
    }

    @Override
    public void run(ApplicationArguments args) {
        courseIngestionService.ingestCourses();
    }

}
