package com.jobresolver.jobresolver;

import com.jobresolver.jobresolver.loader.DataLoader;
import com.jobresolver.jobresolver.model.JobBoard;
import com.jobresolver.jobresolver.model.JobOpportunity;
import com.jobresolver.jobresolver.model.MatchedJobOpportunity;
import com.jobresolver.jobresolver.repository.JobBoardRepository;
import com.jobresolver.jobresolver.repository.JobOpportunityRepository;
import com.jobresolver.jobresolver.repository.MatchedJobOpportunityRepository;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobResolver {

    private static final String UNKNOWN = "Unknown";

    private static final String[] protocols = {"http:", "https:"};

    private final DataLoader dataLoader;

    private final JobBoardRepository jobBoardRepository;

    private final JobOpportunityRepository jobOpportunityRepository;

    private final MatchedJobOpportunityRepository matchedJobOpportunityRepository;

    /**
     * The main method, that triggers the data loading (from JSON and CSV) and matching between the URLs and domains.
     */
    @PostConstruct
    private void resolve() {

        var dataLoadingStatus = dataLoader.initializeJobBoardsAndJobOpportunities();

        if (dataLoadingStatus < 0) {
            log.error("Error with the static loading of data. Check logs for further investigation.");
            return;
        }

        // Fetch all job boards and job opportunities.
        var jobBoards = jobBoardRepository.findAll();
        var jobOpportunities = jobOpportunityRepository.findAll();

        // Resolve the job opportunities with the job boards.
        var matchedJobOpportunities = matchJobOpportunities(jobOpportunities, jobBoards);

        matchedJobOpportunityRepository.saveAll(matchedJobOpportunities);

        // The method to generate the file is commented out, since the data is static.
        // The file name is matched_job_opportunities.csv
        /*
        try {
            writeMatchedJobOpportunitiesToCsv(matchedJobOpportunities);
        } catch (Exception e) {
            log.info("Unsuccessful creation of CSV file. Check logs.");
            e.printStackTrace();
        }

         */
    }

    public List<MatchedJobOpportunity> matchJobOpportunities(List<JobOpportunity> jobOpportunities,
                                                             List<JobBoard> jobBoards) {
        List<MatchedJobOpportunity> matchedJobOpportunities = new ArrayList<>();

        for (var jobOpportunity : jobOpportunities) {

            var matchedJobOpportunity = new MatchedJobOpportunity(jobOpportunity.getId(), jobOpportunity.getJobTitle(),
                    jobOpportunity.getCompanyName(), jobOpportunity.getJobUrl());

            // If we are lacking the job url, we default to UNKNOWN.
            if (jobOpportunity.getJobUrl() == null || jobOpportunity.getJobUrl().length() == 0) {
                matchedJobOpportunity.setJobSource(UNKNOWN);
                matchedJobOpportunities.add(matchedJobOpportunity);
                continue;
            }

            String urlPartToCheck = getUrlPart(jobOpportunity.getJobUrl());

            // If we lack subdomain and/or domain, we default to UNKNOWN.
            if (urlPartToCheck == null) {
                matchedJobOpportunity.setJobSource(UNKNOWN);
                matchedJobOpportunities.add(matchedJobOpportunity);
                continue;
            }

            // Try to match the URL to job board.
            String jobSource = matchJobUrlToJobBoard(jobBoards, urlPartToCheck);

            // Then, we try with Company name.
            if (jobSource.equals(UNKNOWN)) {
                jobSource = matchJobUrlToCompanyName(urlPartToCheck, jobOpportunity.getCompanyName());
            }

            // Set the jobSource and add the matched opportunity.
            matchedJobOpportunity.setJobSource(jobSource);
            matchedJobOpportunities.add(matchedJobOpportunity);
        }

        return matchedJobOpportunities;
    }

    private String matchJobUrlToCompanyName(String urlPartToCheck, String companyName) {
        return (urlPartToCheck.toLowerCase().contains(companyName.toLowerCase())) ? "Company Website" : UNKNOWN;
    }

    /**
     * Extracts the subdomain and domain part of the URL.
     * @param jobUrl - the URL to be extracted from
     *
     * @return - extracted subdomain and domain.
     *
     */
    private String getUrlPart(String jobUrl) {

        String[] urlParts = jobUrl.split("/");

        if (urlParts.length == 0) {
            return null;
        }

        if ((urlParts[0].equals(protocols[0]) || urlParts[0].equals(protocols[1])) && urlParts.length > 2) {
            return urlParts[2];
        }

        return urlParts[0];
    }

    private String matchJobUrlToJobBoard(List<JobBoard> jobBoards, String urlPartToCheck) {

        for (var jobBoard : jobBoards) {
            if (urlPartToCheck.endsWith(jobBoard.getRootDomain())) {
                return jobBoard.getName();
            }
        }

        return UNKNOWN;
    }

    private void writeMatchedJobOpportunitiesToCsv(List<MatchedJobOpportunity> matchedJobOpportunities) throws Exception {

        FileWriter writer = new FileWriter("matched_job_opportunities.csv");

        // Write matched jobs to csv.
        StatefulBeanToCsv<MatchedJobOpportunity> beanToCsv =
                new StatefulBeanToCsvBuilder<MatchedJobOpportunity>(writer).build();
        beanToCsv.write(matchedJobOpportunities);

        writer.close();
    }

}
