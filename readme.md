This guide will get you up and running, and show you how to use the API.

Getting Started
First things first, let's get this thing running on your machine.

Prerequisites
You don't need much, just the essentials:

Java 17+: Make sure your JAVA_HOME is set up correctly.

Maven: For building the project and managing dependencies.

Docker: The easiest way to get an Elasticsearch instance running. If you don't have Docker, you'll need to install and run Elasticsearch 8.x manually.

1. Run Elasticsearch
   We need a search engine. Pop open a terminal and run this Docker command:

docker compose up -d

This command pulls the Elasticsearch image and starts it up with security disabled, which is perfect for local development. Leave this terminal window running.

2. Run the Application
   Now, in a new terminal window, navigate to the root directory of this project and kick off the Spring Boot application with Maven.

mvn spring-boot:run

That's it. The first time you run it, the app will automatically:

Connect to your local Elasticsearch instance.

Create a new index called courses with the correct mappings for search and autocomplete.

Load the sample data from src/main/resources/sample-courses.json into the index.

On subsequent runs, it will see that the index already exists and skip the setup process.

API Endpoints
The application exposes two main endpoints. You can hit them with curl, Postman, or any other API client.

Search Endpoint
This is simply used for seaching courses. It's super flexible and lets you combine a bunch of different filters.

URL: GET /api/search

Success Response: 200 OK with a JSON object containing the total number of hits and a list of course summaries.

Query Parameters:

Parameter

Type

Description

q

string

A search term. Hits title and description. Supports fuzzy matching for typos.

minAge

integer

Filters for courses suitable for this minimum age.

maxAge

integer

Filters for courses suitable for this maximum age.

category

string

Filters by an exact category (e.g., "Science", "Music").

type

string

Filters by an exact course type (e.g., "CLUB", "ONE_TIME").

minPrice

double

The minimum price for a course.

maxPrice

double

The maximum price for a course.

startDate

date

Find courses starting on or after this date (YYYY-MM-DD).

sort

string

Sort order. Options: priceAsc, priceDesc. Defaults to upcoming (by nextSessionDate).

page / size

integer

Standard pagination controls. Defaults to page=0, size=10.

Examples:

Fuzzy search for a typo like "algerba":

curl "http://localhost:8080/api/search?q=algerba"

Find all "Music" courses under $100:

curl "http://localhost:8080/api/search?category=Music&maxPrice=100"

Find "CLUB" courses for a 10-year-old, sorted by price (cheapest first):

curl "http://localhost:8080/api/search?type=CLUB&minAge=10&maxAge=10&sort=priceAsc"

Autocomplete Endpoint
This endpoint gives you instant title suggestions as a user types. It's designed to be hit on every keystroke in a search bar.

URL: GET /api/search/suggest

Success Response: 200 OK with a simple JSON array of matching course titles.

Query Parameters:

Parameter

Type

Description

q

string

Required. The prefix or partial title.

Examples:

Get suggestions for "Intro":

curl "http://localhost:8080/api/search/suggest?q=Intro"

Expected Response: ["Intro to Biology", "Intro to Python Programming", "Intro to Poetry"]

Get suggestions for "Bio":

curl "http://localhost:8080/api/search/suggest?q=Bio"

Expected Response: `["Essentials of Biology", "Intro to Biology", "Advanced Biology", "Workshop in
