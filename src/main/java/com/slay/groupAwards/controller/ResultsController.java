package com.slay.groupAwards.controller;

import com.slay.groupAwards.service.ResultService;
import com.slay.groupAwards.service.dto.NominationResultsView;
import com.slay.groupAwards.repository.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ResultsController {

    private final ResultService resultService;

    public ResultsController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/results")
    public String resultsPage(Model model, Authentication authentication) {
        // currentUser для хедера
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User springUser) {
            // если ты где-то уже делаешь маппинг на свою сущность User – можно переиспользовать,
            // тут просто кладём username в модель, чтобы в хедере не упало
            model.addAttribute("currentUserLogin", springUser.getUsername());
        }

        List<NominationResultsView> items = resultService.getClosedNominationResults();
        model.addAttribute("items", items);

        return "results";
    }
}
