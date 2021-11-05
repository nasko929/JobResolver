package com.jobresolver.jobresolver.model;

import com.opencsv.bean.CsvBindByName;
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
    @CsvBindByName(column = "ID (Primary Key)")
    private Long id;

    @CsvBindByName(column = "Job Title")
    private String jobTitle;

    @CsvBindByName(column = "Company Name")
    private String companyName;

    @CsvBindByName(column = "Job URL")
    private String jobUrl;

    @CsvBindByName(column = "Job Source")
    private String jobSource;

    public MatchedJobOpportunity(Long id, String jobTitle, String companyName, String jobUrl) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.jobUrl = jobUrl;
    }
}