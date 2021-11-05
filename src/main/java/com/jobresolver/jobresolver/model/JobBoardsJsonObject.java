package com.jobresolver.jobresolver.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class JobBoardsJsonObject {

    @JsonAlias("job_boards")
    private List<JobBoard> jobBoards;
}
