[![Build Status](https://travis-ci.org/garystafford/fav-color-service.svg?branch=master)](https://travis-ci.org/garystafford/fav-color-service)

# Voter Service

_Work in Progress_

## Introduction

A simple Spring Boot RESTful microservice, backed by MongoDB.
## Quick Start

To clone, build, test, and run the Voter Spring Boot service locally, requires MongoDB to be pre-installed and running on port `27017`.

```bash
git clone https://github.com/garystafford/voter-service.git && \
    cd voter-service
./gradlew clean cleanTest build && \
    java -jar build/libs/voter-service-0.1.0.jar
```

## Primary Service Endpoints
Out of the box, the service runs on `localhost`, port `8099`. By default, the service looks for MongoDB on `localhost`, port `27017`.

- Purge and Add New Sample Data (GET): <http://localhost:8099/seeder>
- List Candidates (GET): <http://localhost:8099/votes>
- Submit Vote (POST): <http://localhost:8099/votes>
- View Voting Results (GET): <http://localhost:8099/results>
- View Winner (GET): <http://localhost:8099/favorite>
- Service Health (GET): <http://localhost:8099/health>
- Service Metrics (GET): <http://localhost:8099/metrics>
- Other [Spring Actuator](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready) endpoints include: `/mappings`, `/env`, `/configprops`, etc.
- Other [HATEOAS](https://spring.io/guides/gs/rest-hateoas) endpoints for `/votes` include: DELETE, PATCH, PUT, page sort, size, etc.

## POST Vote:
- HTTPie: `http POST localhost:8099/votes vote="Hillary Clinton"`
- cURL: `curl -X POST -H "Content-Type: application/json" -d '{ "vote": "Hillary Clinton" }' "http://localhost:8099/votes"`
- wget: `wget --method POST --header 'content-type: application/json' --body-data '{ "vote": "Hillary Clinton" }' --output-document - http://localhost:8099/votes`

## README
- [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest/)
- [Spring Boot Testing](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-testing)