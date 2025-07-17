package com.quicksearch.quicksearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class IndexService {

    private final ElasticsearchClient client;

    public IndexService(ElasticsearchClient client) {
        this.client = client;
    }

    /**
     * This method runs on application startup. It checks if the "courses" index
     * exists.
     * If it does not, it creates the index with the correct mappings required for
     * the application's search and autocomplete features.
     */
    @PostConstruct
    public void createIndexIfNotExists() {
        try {
            // Check if the index already exists
            boolean exists = client.indices().exists(e -> e.index("courses")).value();

            if (!exists) {
                System.out.println("Index 'courses' does not exist. Creating with mappings...");

                // Create the index with explicit mappings for each field
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
                                // This is the critical mapping for the completion suggester
                                .properties("suggest", p -> p.completion(s -> s))));
                System.out.println("✅ Index 'courses' created successfully.");
            } else {
                System.out.println("Index 'courses' already exists. Skipping creation.");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to create or check index 'courses': " + e.getMessage());
            e.printStackTrace();
        }
    }
}
