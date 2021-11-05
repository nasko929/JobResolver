package com.jobresolver.jobresolver;

import com.jobresolver.jobresolver.model.JobBoard;
import com.jobresolver.jobresolver.model.JobOpportunity;
import lombok.var;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class JobResolverApplicationTests {

    @InjectMocks
    JobResolver jobResolver;

    @Test
    void shouldEmptyArraysReturnEmptyArray() {
        var answer = jobResolver.matchJobOpportunities(new ArrayList<>(), new ArrayList<>());

        assertEquals(answer, new ArrayList<>());
    }

    @Test
    void shouldMapUnknown() {

        var jobOpportunity =
                new JobOpportunity(1L, "SWE", "Nasko Company", "https://random.org");

        var jobBoard = new JobBoard(1L, "Amazon", "Okay", "https://amazon.com", "", "");


        var answer =
                jobResolver.matchJobOpportunities(Collections.singletonList(jobOpportunity),
                        Collections.singletonList(jobBoard));

        assertEquals(answer.size(), 1);
        assertEquals(answer.get(0).getJobSource(), "Unknown");
    }

    @Test
    void shouldMapJobBoard() {
        var jobOpportunity =
                new JobOpportunity(1L, "SWE", "Google", "https://mail.google.com");

        var jobBoard = new JobBoard(1L, "Google", "Okay", "google.com", "", "");


        var answer =
                jobResolver.matchJobOpportunities(Collections.singletonList(jobOpportunity),
                        Collections.singletonList(jobBoard));

        assertEquals(1, answer.size());
        assertEquals("Google", answer.get(0).getJobSource());
    }

    @Test
    void shouldMapCompanyWebsite() {
        var jobOpportunity =
                new JobOpportunity(1L, "SWE", "Uber", "https://careers.uber.com/?oag0104192=12-401-20t");

        var jobBoard = new JobBoard(1L, "Amazon", "Okay", "amazon.com", "", "");


        var answer =
                jobResolver.matchJobOpportunities(Collections.singletonList(jobOpportunity),
                        Collections.singletonList(jobBoard));

        assertEquals(1, answer.size());
        assertEquals("Company Website", answer.get(0).getJobSource());
    }
}
