package com.jobresolver.jobresolver.repository;

import com.jobresolver.jobresolver.model.JobOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobOpportunityRepository extends JpaRepository<JobOpportunity, Long> {
    List<JobOpportunity> findByCompanyName(String companyName);
}
