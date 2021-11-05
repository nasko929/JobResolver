package com.jobresolver.jobresolver.repository;


import com.jobresolver.jobresolver.model.MatchedJobOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchedJobOpportunityRepository extends JpaRepository<MatchedJobOpportunity, Long> {
    List<MatchedJobOpportunity> findByJobSource(String jobSource);
}
