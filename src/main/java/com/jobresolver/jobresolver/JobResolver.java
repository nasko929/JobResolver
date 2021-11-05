package com.jobresolver.jobresolver;

import com.jobresolver.jobresolver.loader.DataLoader;
import com.jobresolver.jobresolver.model.JobBoard;
import com.jobresolver.jobresolver.model.JobOpportunity;
import com.jobresolver.jobresolver.model.MatchedJobOpportunity;
import com.jobresolver.jobresolver.repository.JobBoardRepository;
import com.jobresolver.jobresolver.repository.JobOpportunityRepository;
import com.jobresolver.jobresolver.repository.MatchedJobOpportunityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

        var matchedJobOpportunities = matchJobOpportunities(jobOpportunities, jobBoards);

        matchedJobOpportunityRepository.saveAll(matchedJobOpportunities);
    }

    public List<MatchedJobOpportunity> matchJobOpportunities(List<JobOpportunity> jobOpportunities,
                                                             List<JobBoard> jobBoards) {
        List<MatchedJobOpportunity> matchedJobOpportunities = new ArrayList<>();

        for (var jobOpportunity : jobOpportunities) {

            var matchedJobOpportunity = new MatchedJobOpportunity(jobOpportunity.getId(), jobOpportunity.getJobTitle(),
                    jobOpportunity.getCompanyName(), jobOpportunity.getJobUrl());

            // If we are lacking job url, we default to UNKNOWN.
            if (jobOpportunity.getJobUrl() == null || jobOpportunity.getJobUrl().length() == 0) {
                matchedJobOpportunity.setJobSource(UNKNOWN);
                matchedJobOpportunities.add(matchedJobOpportunity);
                continue;
            }

            String urlPartToCheck = getUrlPart(jobOpportunity.getJobUrl());

            if (urlPartToCheck == null) {
                matchedJobOpportunity.setJobSource(UNKNOWN);
                matchedJobOpportunities.add(matchedJobOpportunity);
                continue;
            }

            String jobBoard = matchJobUrlToJobBoard(jobBoards, urlPartToCheck);
            matchedJobOpportunity.setJobSource(jobBoard);

            matchedJobOpportunities.add(matchedJobOpportunity);
        }

        return matchedJobOpportunities;
    }

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
}
