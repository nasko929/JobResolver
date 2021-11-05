package com.jobresolver.jobresolver.service;

import com.jobresolver.jobresolver.model.MatchedJobOpportunity;
import com.jobresolver.jobresolver.repository.JobBoardRepository;
import com.jobresolver.jobresolver.repository.MatchedJobOpportunityRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchedJobOpportunityService {

    private final JobBoardRepository jobBoardRepository;

    private final MatchedJobOpportunityRepository matchedJobOpportunityRepository;

    public List<MatchedJobOpportunity> getJobOpportunitiesByJobBoardId(Long id, StringBuilder jobSourceName) {
        var jobBoard = jobBoardRepository.findById(id).orElse(null);

        if (jobBoard == null) {
            return new ArrayList<>();
        }

        var matchedJobOpportunities =
                matchedJobOpportunityRepository.findByJobSource(jobBoard.getName());

        jobSourceName.append(jobBoard.getName());

        return (matchedJobOpportunities != null) ? matchedJobOpportunities : new ArrayList<>();
    }
}
