# QuickSearch API

This guide will get you up and running, and show you how to use the API.

---

## GETTING STARTED

Prerequisites:

- Java 17+
- Maven
- Docker

Notes:

- JAVA_HOME should be set
- Docker is recommended for Elasticsearch 8.x; else install manually

Steps:

1. Run Elasticsearch:
   Command: docker compose up -d
   Note: Keep this terminal running

2. Run the Application:
   Command: mvn spring-boot:run
   Effects:
   - Connects to Elasticsearch
   - Creates `courses` index with mappings
   - Loads sample data from `src/main/resources/sample-courses.json`
   - Skips setup on subsequent runs if index exists

---

## API ENDPOINTS

1. /api/search (GET)
   Description: Search for courses using filters and search term.

   Response:

   - Code: 200 OK
   - Content: JSON with total hits and course summaries

   Query Parameters:

   - q (string): Search term (fuzzy match on title/description)
   - minAge (integer): Minimum suitable age
   - maxAge (integer): Maximum suitable age
   - category (string): Exact category (e.g., "Science")
   - type (string): Exact type (e.g., "CLUB", "ONE_TIME")
   - minPrice (double): Minimum price
   - maxPrice (double): Maximum price
   - startDate (date): Start on or after (YYYY-MM-DD)
   - sort (string): Sort order: priceAsc, priceDesc (default: nextSessionDate)
   - page (integer): Page number (default: 0)
   - size (integer): Page size (default: 10)

   Examples:

   - Fuzzy search for typo:
     curl "http://localhost:8080/api/search?q=algerba"

   - Music courses under $100:
     curl "http://localhost:8080/api/search?category=Music&maxPrice=100"

   - CLUB courses for 10-year-old sorted by price:
     curl "http://localhost:8080/api/search?type=CLUB&minAge=10&maxAge=10&sort=priceAsc"

---

2. /api/search/suggest (GET)
   Description: Returns autocomplete suggestions based on partial input.

   Response:

   - Code: 200 OK
   - Content: JSON array of matching titles

   Query Parameters:

   - q (string, required): Prefix or partial title

   Examples:

   - Suggestions for "Intro":
     curl "http://localhost:8080/api/search/suggest?q=Intro"
     Response:
     ["Intro to Biology", "Intro to Python Programming", "Intro to Poetry"]

   - Suggestions for "Bio":
     curl "http://localhost:8080/api/search/suggest?q=Bio"

---

## You're all set! You now have a powerful search API running with Elasticsearch and Spring Boot.
