package com.slay.groupAwards.controller;

import com.slay.groupAwards.controller.dto.CandidateDto;
import com.slay.groupAwards.controller.dto.CandidateResultDto;
import com.slay.groupAwards.repository.models.Nomination;
import com.slay.groupAwards.service.NominationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nominations")
public class NominationController {

    private final NominationService nominationService;

    public NominationController(NominationService nominationService) {
        this.nominationService = nominationService;
    }

    @GetMapping
    public List<Nomination> getActiveNominations() {
        return nominationService.getActiveNominations();
    }

    @GetMapping("/{id}/candidates")
    public List<CandidateDto> getCandidates(@PathVariable("id") Integer nominationId) {
        return nominationService.getCandidatesForNomination(nominationId);
    }

    @GetMapping("/{id}/results")
    public List<CandidateResultDto> getResults(@PathVariable("id") Integer nominationId) {
        return nominationService.getResultsForNomination(nominationId);
    }

}