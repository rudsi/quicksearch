package com.quicksearch.quicksearch.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseDocument {

    private String id;
    private String title;
    private String description;
    private String category;
    private String type;
    private String gradeRange;
    private int minAge;
    private int maxAge;
    private double price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]'Z'", timezone = "UTC")
    private Instant nextSessionDate;

    private Suggest suggest;

    public CourseDocument() {
    }

    public CourseDocument(String id, String title, String description, String category, String type,
            String gradeRange, int minAge, int maxAge, double price, Instant nextSessionDate,
            Suggest suggest) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.type = type;
        this.gradeRange = gradeRange;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.price = price;
        this.nextSessionDate = nextSessionDate;
        this.suggest = suggest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGradeRange() {
        return gradeRange;
    }

    public void setGradeRange(String gradeRange) {
        this.gradeRange = gradeRange;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Instant getNextSessionDate() {
        return nextSessionDate;
    }

    public void setNextSessionDate(Instant nextSessionDate) {
        this.nextSessionDate = nextSessionDate;
    }

    public Suggest getSuggest() {
        return suggest;
    }

    public void setSuggest(Suggest suggest) {
        this.suggest = suggest;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Suggest {
        private List<String> input;
        private Integer weight;

        public Suggest() {
        }

        public Suggest(List<String> input, Integer weight) {
            this.input = input;
            this.weight = weight;
        }

        public List<String> getInput() {
            return input;
        }

        public void setInput(List<String> input) {
            this.input = input;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }
    }
}
