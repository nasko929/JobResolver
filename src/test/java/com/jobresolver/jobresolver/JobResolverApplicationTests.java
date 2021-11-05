package com.jobresolver.jobresolver;

import lombok.var;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class JobResolverApplicationTests {

    @InjectMocks
    @Resource
    JobResolver jobResolver;

	@Test
	void shouldJobOpportunitiesBeMatchedCorrectly() {
        var answer = jobResolver.matchJobOpportunities(new ArrayList<>(), new ArrayList<>());

        assertEquals(answer, new ArrayList<>());
	}

}
