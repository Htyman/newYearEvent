package com.slay.groupAwards.controller;

import com.slay.groupAwards.repository.CandidateRepository;
import com.slay.groupAwards.repository.models.Nomination;
import com.slay.groupAwards.service.AdminReportService;
import com.slay.groupAwards.service.NominationService;
import com.slay.groupAwards.service.dto.UserVoteStatusDto;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminWebController {

    private final NominationService nominationService;
    private final CandidateRepository candidateRepository;
    private final AdminReportService adminReportService;

    public AdminWebController(NominationService nominationService, CandidateRepository candidateRepository,
                              AdminReportService adminReportService) {
        this.nominationService = nominationService;
        this.candidateRepository = candidateRepository;
        this.adminReportService = adminReportService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminPanel(Model model) {
        List<Nomination> nominations = nominationService.getAllNominations();
        model.addAttribute("nominations", nominations);
        return "admin";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/candidates")
    public String adminCandidates(Model model) {
        model.addAttribute("candidates", candidateRepository.findAll());
        return "admin-candidates";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public String userVotesPage(Model model) {
        List<UserVoteStatusDto> statuses = adminReportService.getUserVoteStatuses();
        model.addAttribute("statuses", statuses);
        return "admin-users";
    }
}
