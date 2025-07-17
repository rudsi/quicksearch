project: QuickSearch API
description: >
This guide will get you up and running, and show you how to use the API.

getting_started:
prerequisites: - Java 17+ - Maven - Docker
notes: - JAVA_HOME should be set - Docker is recommended for Elasticsearch 8.x; else install manually

steps: - step: Run Elasticsearch
command: docker compose up -d
note: Keep this terminal running

    - step: Run the Application
      command: mvn spring-boot:run
      effects:
        - Connects to Elasticsearch
        - Creates `courses` index with mappings
        - Loads sample data from `src/main/resources/sample-courses.json`
        - Skips setup on subsequent runs if index exists

api_endpoints:

- endpoint: /api/search
  method: GET
  description: >
  Search for courses using filters and search term.
  response:
  code: 200 OK
  content: JSON with total hits and course summaries
  query_parameters:

  - name: q
    type: string
    description: Search term (fuzzy match on title/description)
  - name: minAge
    type: integer
    description: Minimum suitable age
  - name: maxAge
    type: integer
    description: Maximum suitable age
  - name: category
    type: string
    description: Exact category (e.g., "Science")
  - name: type
    type: string
    description: Exact type (e.g., "CLUB", "ONE_TIME")
  - name: minPrice
    type: double
    description: Minimum price
  - name: maxPrice
    type: double
    description: Maximum price
  - name: startDate
    type: date
    description: Start on or after (YYYY-MM-DD)
  - name: sort
    type: string
    description: Sort order: priceAsc, priceDesc (default: nextSessionDate)
  - name: page
    type: integer
    description: Page number (default: 0)
  - name: size
    type: integer
    description: Page size (default: 10)
    examples:
  - description: Fuzzy search for typo
    curl: curl "http://localhost:8080/api/search?q=algerba"
  - description: Music courses under $100
    curl: curl "http://localhost:8080/api/search?category=Music&maxPrice=100"
  - description: CLUB courses for 10-year-old sorted by price
    curl: curl "http://localhost:8080/api/search?type=CLUB&minAge=10&maxAge=10&sort=priceAsc"

- endpoint: /api/search/suggest
  method: GET
  description: >
  Returns autocomplete suggestions based on partial input.
  response:
  code: 200 OK
  content: JSON array of matching titles
  query_parameters:
  - name: q
    type: string
    required: true
    description: Prefix or partial title
    examples:
  - description: Suggestions for "Intro"
    curl: curl "http://localhost:8080/api/search/suggest?q=Intro"
    response:
    - Intro to Biology
    - Intro to Python Programming
    - Intro to Poetry
  - description: Suggestions for "Bio"
    curl: curl "http://localhost:8080/api/search/suggest?q=Bio"

final_note: You're all set! You now have a powerful search API running with Elasticsearch and Spring Boot.
