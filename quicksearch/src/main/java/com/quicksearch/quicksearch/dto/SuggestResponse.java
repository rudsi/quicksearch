package com.quicksearch.quicksearch.dto;

import java.util.List;

public class SuggestResponse {
    private List<String> suggestions;

    public SuggestResponse() {
    }

    public SuggestResponse(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
}
