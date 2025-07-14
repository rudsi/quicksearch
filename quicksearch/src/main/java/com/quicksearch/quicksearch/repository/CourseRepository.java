package com.quicksearch.quicksearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.quicksearch.quicksearch.document.CourseDocument;

public interface CourseRepository extends ElasticsearchRepository<CourseDocument, String> {

}