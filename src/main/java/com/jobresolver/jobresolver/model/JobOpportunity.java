package com.jobresolver.jobresolver.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "JOB_OPPORTUNITIES")
public class JobOpportunity {

    @Id
    @CsvBindByName(column = "ID (primary key)")
    private Long id;

    @CsvBindByName(column = "Job Title")
    private String jobTitle;

    @CsvBindByName(column = "Company Name")
    private String companyName;

    @CsvBindByName(column = "Job URL")
    private String jobUrl;
}