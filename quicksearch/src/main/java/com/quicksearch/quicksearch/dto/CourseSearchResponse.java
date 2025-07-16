package com.quicksearch.dto;

import java.time.LocalDate;
import java.util.List;

public class CourseSearchResponse {

    private long total;
    private List<CourseSummary> courses;

    public CourseSearchResponse(long total, List<CourseSummary> courses) {
        this.total = total;
        this.courses = courses;
    }

    public long getTotal() {
        return total;
    }

    public List<CourseSummary> getCourses() {
        return courses;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setCourses(List<CourseSummary> courses) {
        this.courses = courses;
    }

    public static class CourseSummary {
        private String id;
        private String title;
        private String category;
        private double price;
        private LocalDate nextSessionDate;

        public CourseSummary(String id, String title, String category, double price, LocalDate nextSessionDate) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.price = price;
            this.nextSessionDate = nextSessionDate;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getCategory() {
            return category;
        }

        public double getPrice() {
            return price;
        }

        public LocalDate getNextSessionDate() {
            return nextSessionDate;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public void setNextSessionDate(LocalDate nextSessionDate) {
            this.nextSessionDate = nextSessionDate;
        }
    }
}
