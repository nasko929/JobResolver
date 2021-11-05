package com.jobresolver.jobresolver.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
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