package com.quicksearch.quicksearch.dto;

import java.time.LocalDate;

public class CourseSearchRequest {

    private String query;
    private Integer minAge;
    private Integer maxAge;
    private Double minPrice;
    private Double maxPrice;
    private String category;
    private String type;
    private LocalDate nextSessionDate;
    private String sort;
    private int page = 0;
    private int size = 10;

    public String getQuery() {
        return query;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public LocalDate getNextSessionDate() {
        return nextSessionDate;
    }

    public String getSort() {
        return sort;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNextSessionDate(LocalDate nextSessionDate) {
        this.nextSessionDate = nextSessionDate;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public CourseSearchRequest() {
    }
}
