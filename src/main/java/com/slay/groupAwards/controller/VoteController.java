package com.slay.groupAwards.controller;

import com.slay.groupAwards.controller.dto.VoteRequest;
import com.slay.groupAwards.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<?> vote(@RequestBody VoteRequest request) {
        voteService.vote(request.getNominationId(), request.getCandidateId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{nominationId}")
    public ResponseEntity<?> cancel(@PathVariable Integer nominationId) {
        voteService.cancelVote(nominationId);
        return ResponseEntity.noContent().build();
    }
}
