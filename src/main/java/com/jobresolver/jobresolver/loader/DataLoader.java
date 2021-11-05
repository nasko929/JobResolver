package com.jobresolver.jobresolver.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobresolver.jobresolver.model.JobBoard;
import com.jobresolver.jobresolver.model.JobBoardsJsonObject;
import com.jobresolver.jobresolver.model.JobOpportunity;
import com.jobresolver.jobresolver.repository.JobBoardRepository;
import com.jobresolver.jobresolver.repository.JobOpportunityRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader {

    @Value("classpath:static/jobBoards.json")
    private Resource jobBoardsFile;

    @Value("classpath:static/job_opportunities.csv")
    private Resource jobOpportunitiesFile;

    private final JobBoardRepository jobBoardRepository;

    private final JobOpportunityRepository jobOpportunityRepository;

    public int initializeJobBoardsAndJobOpportunities() {

        List<JobBoard> jobBoards;
        List<JobOpportunity> jobOpportunities;

        // Try to parse the Job Boards from the JSON.
        try {
            jobBoards = readJobBoardsJson();
            jobOpportunities = readJobOpportunitiesCsv();
        } catch (Exception e) {
            log.error("Unable to open Job Boards and/or Job Opportunities from JSON. The stack trace is: ");
            e.printStackTrace();
            return -1;
        }

        jobBoardRepository.saveAll(jobBoards);
        jobOpportunityRepository.saveAll(jobOpportunities);

        log.info("Successfully imported all Job Boards and Job Opportunities to H2 Database.");
        return 1;
    }

    private List<JobOpportunity> readJobOpportunitiesCsv() throws IOException {

        // Use Buffered Reader to read the Input Stream from the CSV.
        Reader reader = new BufferedReader(new InputStreamReader(jobOpportunitiesFile.getInputStream()));

        // Use the reader to create CSV Builder and easily parse the data.
        CsvToBean<JobOpportunity> jobOpportunityCsv = new CsvToBeanBuilder<JobOpportunity>(reader)
                .withType(JobOpportunity.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        return jobOpportunityCsv.parse();
    }

    private List<JobBoard> readJobBoardsJson() throws IOException {

        // Create ObjectMapper instance.
        ObjectMapper objectMapper = new ObjectMapper();

        // Get the string with JSON content.
        String jsonContent = getContent(jobBoardsFile);

        // Read the JSON string and convert to JobBoards object.
        JobBoardsJsonObject jobBoardsJsonObject = objectMapper.readValue(jsonContent, JobBoardsJsonObject.class);

        // Return the list of jobBoards.
        return jobBoardsJsonObject.getJobBoards();
    }

    private String getContent(Resource resource) throws IOException {
        byte[] jsonBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());

        return new String(jsonBytes, StandardCharsets.UTF_8);
    }
}