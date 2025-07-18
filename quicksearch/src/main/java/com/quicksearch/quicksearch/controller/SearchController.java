package com.quicksearch.quicksearch.controller;

import java.time.LocalDate;
import java.util.List;

import com.quicksearch.quicksearch.dto.CourseSearchResponse;
import com.quicksearch.quicksearch.service.SearchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public CourseSearchResponse search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(defaultValue = "upcoming") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {

        return searchService.searchCourses(
                q, minAge, maxAge, category, type, minPrice, maxPrice, startDate, sort, page, size);
    }

    @GetMapping("/search/suggest")
    public List<String> suggest(@RequestParam("q") String prefix) throws Exception {
        return searchService.suggestTitles(prefix);
    }
}
