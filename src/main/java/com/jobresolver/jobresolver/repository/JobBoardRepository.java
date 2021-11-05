package com.jobresolver.jobresolver.repository;

import com.jobresolver.jobresolver.model.JobBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobBoardRepository extends JpaRepository<JobBoard, Long> {
}
