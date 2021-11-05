package com.jobresolver.jobresolver.controller;

import com.jobresolver.jobresolver.repository.JobBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jobBoards")
public class JobBoardController {

    private final JobBoardRepository jobBoardRepository;

    @GetMapping
    public String showAllJobBoards(Model model) {
        model.addAttribute("jobBoards", jobBoardRepository.findAll());
        return "jobBoards";
    }
}
