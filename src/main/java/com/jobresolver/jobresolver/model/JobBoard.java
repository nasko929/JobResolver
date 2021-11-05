package com.jobresolver.jobresolver.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "JOB_BOARDS")
public class JobBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String rating;

    @JsonAlias("root_domain")
    private String rootDomain;

    @JsonAlias("logo_file")
    private String logoFile;

    private String description;
}