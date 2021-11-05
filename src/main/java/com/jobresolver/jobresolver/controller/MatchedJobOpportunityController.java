package com.jobresolver.jobresolver.controller;

import com.jobresolver.jobresolver.service.MatchedJobOpportunityService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/jobOpportunity")
public class MatchedJobOpportunityController {

    private final MatchedJobOpportunityService matchedJobOpportunityService;

    @GetMapping("/{id}")
    public String showAllJobOpportunities(Model model, @PathVariable Long id) {
        StringBuilder jobSourceName = new StringBuilder();
        var matchedJobOpportunities = matchedJobOpportunityService.getJobOpportunitiesByJobBoardId(id, jobSourceName);
        model.addAttribute("jobOpportunities", matchedJobOpportunities);
        model.addAttribute("numberOfJobOpportunities", matchedJobOpportunities.size());
        model.addAttribute("jobSource", jobSourceName.toString());

        return "jobOpportunities";
    }
}
