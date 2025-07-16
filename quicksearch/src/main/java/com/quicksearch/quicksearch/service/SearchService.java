package com.quicksearch.quicksearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.quicksearch.dto.CourseSearchResponse;
import com.quicksearch.quicksearch.document.CourseDocument;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    private final ElasticsearchClient client;

    public SearchService(ElasticsearchClient client) {
        this.client = client;
    }

    public CourseSearchResponse searchCourses(
            String q, Integer minAge, Integer maxAge, String category, String type,
            Double minPrice, Double maxPrice, LocalDate startDate, String sort, int page, int size) throws Exception {

        List<Query> must = new ArrayList<>();
        List<Query> filters = new ArrayList<>();

        if (q != null && !q.isBlank()) {
            must.add(Query.of(m -> m.multiMatch(mm -> mm.fields("title", "description").query(q))));
        }

        if (minAge != null) {
            filters.add(Query.of(f -> f.range(r -> r.field("minAge").gte(JsonData.of(minAge)))));
        }
        if (maxAge != null) {
            filters.add(Query.of(f -> f.range(r -> r.field("maxAge").lte(JsonData.of(maxAge)))));
        }

        if (minPrice != null || maxPrice != null) {
            filters.add(Query.of(f -> f.range(r -> {
                r.field("price");
                if (minPrice != null)
                    r.gte(JsonData.of(minPrice));
                if (maxPrice != null)
                    r.lte(JsonData.of(maxPrice));
                return r;
            })));
        }

        if (category != null && !category.isBlank()) {
            filters.add(Query.of(t -> t.term(v -> v.field("category").value(category))));
        }

        if (type != null && !type.isBlank()) {
            filters.add(Query.of(t -> t.term(v -> v.field("type").value(type))));
        }

        if (startDate != null) {
            filters.add(Query.of(f -> f.range(r -> {
                r.field("nextSessionDate");
                r.gte(JsonData.of(startDate.toString()));
                return r;
            })));
        }

        final String finalSortField;
        final SortOrder finalSortOrder;

        if (sort != null) {
            if ("priceAsc".equalsIgnoreCase(sort)) {
                finalSortField = "price";
                finalSortOrder = SortOrder.Asc;
            } else if ("priceDesc".equalsIgnoreCase(sort)) {
                finalSortField = "price";
                finalSortOrder = SortOrder.Desc;
            } else {
                finalSortField = "nextSessionDate";
                finalSortOrder = SortOrder.Asc;
            }
        } else {
            finalSortField = "nextSessionDate";
            finalSortOrder = SortOrder.Asc;
        }

        Query finalQuery = Query.of(qb -> qb.bool(b -> b.must(must).filter(filters)));

        System.out.println("Final query: " + finalQuery.toString());

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("courses")
                .query(finalQuery)
                .from(page * size)
                .size(size)
                .sort(st -> st.field(f -> f.field(finalSortField).order(finalSortOrder))));

        SearchResponse<CourseDocument> response = client.search(searchRequest, CourseDocument.class);

        List<CourseSearchResponse.CourseSummary> results = new ArrayList<>();
        for (Hit<CourseDocument> hit : response.hits().hits()) {
            CourseDocument doc = hit.source();
            if (doc == null)
                continue;

            LocalDate date = doc.getNextSessionDate() != null
                    ? doc.getNextSessionDate().atZone(ZoneId.systemDefault()).toLocalDate()
                    : null;

            results.add(new CourseSearchResponse.CourseSummary(
                    doc.getId(),
                    doc.getTitle(),
                    doc.getCategory(),
                    doc.getPrice(),
                    date));
        }

        long totalHits = response.hits().total() != null ? response.hits().total().value() : 0;
        return new CourseSearchResponse(totalHits, results);
    }
}
