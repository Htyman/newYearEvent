package com.slay.groupAwards.controller;

import com.slay.groupAwards.controller.dto.CandidateDto;
import com.slay.groupAwards.exception.AlreadyVotedException;
import com.slay.groupAwards.exception.NotFoundException;
import com.slay.groupAwards.repository.models.Candidate;
import com.slay.groupAwards.repository.models.CandidateInNomination;
import com.slay.groupAwards.repository.models.Nomination;
import com.slay.groupAwards.repository.models.Vote;
import com.slay.groupAwards.service.NominationService;
import com.slay.groupAwards.service.VoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    private final NominationService nominationService;
    private final VoteService voteService;

    public WebController(NominationService nominationService,
                         VoteService voteService) {
        this.nominationService = nominationService;
        this.voteService = voteService;
    }

    @GetMapping("/nominations")
    public String nominations(Model model) {
        List<Nomination> nominations = nominationService.getActiveNominations();
        model.addAttribute("nominations", nominations);
        return "nominations"; // templates/nominations.html
    }

    @GetMapping("/nominations/{id}")
    public String nomination(@PathVariable("id") Integer id,
                             @RequestParam(value = "error", required = false) String error,
                             Model model) {
        Nomination nomination = nominationService.getNominationById(id);
        List<CandidateDto> candidates = nominationService.getCandidatesForNomination(id);

        model.addAttribute("nomination", nomination);
        model.addAttribute("candidates", candidates);
        model.addAttribute("error", error);

        // информация о голосе пользователя
        Optional<Vote> userVoteOpt = Optional.empty();
        try {
            userVoteOpt = voteService.getUserVoteForNomination(id);
        } catch (Exception ignored) {
            // если не залогинен или что-то ещё — просто считаем, что голоса нет
        }

        boolean userVoted = userVoteOpt.isPresent();
        model.addAttribute("userVoted", userVoted);

        if (userVoted) {
            Vote vote = userVoteOpt.get();
            CandidateInNomination cin = vote.getCandidateInNomination();
            Candidate candidate = cin.getCandidate();

            model.addAttribute("userVoteCandidateId", candidate.getId());
            String fullname = (candidate.getName() != null ? candidate.getName() : "") +
                    " " +
                    (candidate.getSurname() != null ? candidate.getSurname() : "");
            model.addAttribute("userVoteCandidateName", fullname.trim());
        }

        // результаты только если номинация закрыта
        if (!nomination.isActive()) {
            var results = nominationService.getResultsForNomination(id);
            model.addAttribute("results", results);
        } else {
            model.addAttribute("results", List.of());
        }

        return "nomination";
    }


    @PostMapping("/nominations/{id}/vote")
    public String vote(@PathVariable("id") Integer nominationId,
                       @RequestParam("candidateId") Integer candidateId) {
        try {
            voteService.vote(nominationId, candidateId);
            // после успешного голосования можно показать результаты
            return "redirect:/nominations/" + nominationId;
        } catch (AlreadyVotedException ex) {
            return "redirect:/nominations/" + nominationId + "?error=already_voted";
        } catch (NotFoundException ex) {
            return "redirect:/nominations/" + nominationId + "?error=not_found";
        } catch (IllegalStateException ex) {
            return "redirect:/nominations/" + nominationId + "?error=closed";
        } catch (Exception ex) {
            return "redirect:/nominations/" + nominationId + "?error=unknown";
        }
    }
}
