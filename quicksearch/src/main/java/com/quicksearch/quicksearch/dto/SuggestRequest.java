package com.quicksearch.quicksearch.dto;

public class SuggestRequest {
    private String q;

    public SuggestRequest() {
    }

    public SuggestRequest(String q) {
        this.q = q;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
