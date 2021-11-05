# JobResolver
A job resolver to match some job opportunities to job providers.

## Instructions

* The top-level file that contains the main logic is `JobResolver.java`;
* The matched job opportunities csv is in the root folder under the name `matched_job_opportunities.csv`;
* The visual representation of job sources and number of associated job opportunities is under the name `job_sources_info.txt`.

## How it works
* First, we read and save all the data from the provided files (*job_opportunities.csv and jobBoards.json*);
* We store them in an in-memory H2 database;
* Then we map them using the following algorithm:
    1. We take the job URL and extract the subdomain and domain from it;
    2. We use the String method `endsWith` to match the domain with the extracted part of the URL;
    3. In case none of the Job Boards matches, we try to match the extracted part by Company Name, using `contains`;
    4. If no matches occur, we proceed to set Job Source as `Unknown`.
    
## Effectiveness of the solution

The time complexity is as follows:

* When we use `endsWith` method to check, we do overall O(*jobUrlDomainSize* * *jobBoardsCount*) which in our case is quite effective.
* When we cannot use `endsWith`, we use `contains`, but we use it only once for checking with company name, therefore the complexity here is O(*jobUrlDomainSize* * *companyNameSize*). A slow solution, gladly executed only once.
* Overall, the time complexity is O(sum(*jobUrlDomainsSize*) * *jobBoardsCount*)

## Tech Stack

The used the following technologies:
* **Spring Boot** for the BE part;
* **Thymeleaf** for the FE part;
* **H2 in-memory database** for DB;
* **Spring Data JPA** for mapping between DB and BE.

## Third-party Libraries and Dependencies

The used third-party dependencies are:
* **JUnit** and **Mockito** for the unit tests;
* **Lombok** for avoiding boilerplate code;
* **OpenCSV** for writing and reading CSVs;

## Tasks to implement

* Q: How can we determine if a job application link is still active?
* A: We ping it and expect successful request, as well as 2XX HTTP code.
---
* Q: How can we scrape and store the location from a job description?
* A: Once we successfully ping the URL, we will receive the HTML of the page, we might as well search for a DOM element with id `location` or search for the word `Location` itself.

## Tasks to think about

* How can we build a search engine that allows us to find jobs based on location and radius?