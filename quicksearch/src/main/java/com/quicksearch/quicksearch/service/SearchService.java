package com.quicksearch.quicksearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.quicksearch.quicksearch.document.CourseDocument;
import com.quicksearch.quicksearch.dto.CourseSearchResponse;
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
            must.add(Query.of(m -> m
                    .multiMatch(mm -> mm
                            .fields("title", "description")
                            .query(q)
                            .fuzziness("AUTO"))));
        }

        if (minAge != null) {
            filters.add(Query.of(f -> f.range(r -> r.field("maxAge").gte(JsonData.of(minAge)))));
        }
        if (maxAge != null) {
            filters.add(Query.of(f -> f.range(r -> r.field("minAge").lte(JsonData.of(maxAge)))));
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
            filters.add(Query.of(f -> f.range(r -> r.field("nextSessionDate").gte(JsonData.of(startDate.toString())))));
        }

        final String finalSortField;
        final SortOrder finalSortOrder;

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

        Query finalQuery = Query.of(qb -> qb.bool(b -> b.must(must).filter(filters)));

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

    public List<String> suggestTitles(String prefix) throws Exception {
        SearchRequest request = SearchRequest.of(s -> s
                .index("courses")
                .suggest(sg -> sg
                        .suggesters("title-suggest", sgs -> sgs
                                .prefix(prefix)
                                .completion(c -> c
                                        .field("suggest")
                                        .skipDuplicates(true)
                                        .size(10)))));

        SearchResponse<Void> response = client.search(request, Void.class);

        return response.suggest()
                .get("title-suggest").stream()
                .flatMap(suggestion -> suggestion.completion().options().stream())
                .map(opt -> opt.text())
                .distinct()
                .toList();
    }

}
