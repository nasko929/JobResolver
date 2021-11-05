package com.jobresolver.jobresolver.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@Table(name = "MATCHED_JOB_OPPORTUNITIES")
public class MatchedJobOpportunity {

    @Id
    private Long id;

    private String jobTitle;

    private String companyName;

    private String jobUrl;

    private String jobSource;

    public MatchedJobOpportunity(Long id, String jobTitle, String companyName, String jobUrl) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.jobUrl = jobUrl;
    }
}