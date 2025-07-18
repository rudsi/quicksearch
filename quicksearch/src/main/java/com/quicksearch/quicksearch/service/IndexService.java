package com.quicksearch.quicksearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class IndexService {

    private final ElasticsearchClient client;

    public IndexService(ElasticsearchClient client) {
        this.client = client;
    }

    @PostConstruct
    public void createIndexIfNotExists() {
        try {

            boolean exists = client.indices().exists(e -> e.index("courses")).value();

            if (!exists) {
                System.out.println("Index 'courses' does not exist. Creating with mappings...");

                client.indices().create(c -> c
                        .index("courses")
                        .mappings(m -> m
                                .properties("id", p -> p.keyword(k -> k))
                                .properties("title", p -> p.text(t -> t))
                                .properties("description", p -> p.text(t -> t))
                                .properties("category", p -> p.keyword(k -> k))
                                .properties("type", p -> p.keyword(k -> k))
                                .properties("gradeRange", p -> p.keyword(k -> k))
                                .properties("minAge", p -> p.integer(i -> i))
                                .properties("maxAge", p -> p.integer(i -> i))
                                .properties("price", p -> p.double_(d -> d))
                                .properties("nextSessionDate", p -> p.date(d -> d))

                                .properties("suggest", p -> p.completion(s -> s))));
                System.out.println("Index 'courses' created successfully.");
            } else {
                System.out.println("Index 'courses' already exists. Skipping creation.");
            }
        } catch (Exception e) {
            System.err.println("Failed to create or check index 'courses': " + e.getMessage());
            e.printStackTrace();
        }
    }
}
