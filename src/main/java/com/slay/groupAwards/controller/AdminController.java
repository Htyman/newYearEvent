package com.slay.groupAwards.controller;

import com.slay.groupAwards.controller.dto.CandidateCreateRequest;
import com.slay.groupAwards.controller.dto.NominationCreateRequest;
import com.slay.groupAwards.repository.models.Candidate;
import com.slay.groupAwards.repository.models.Nomination;
import com.slay.groupAwards.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/nominations")
    public ResponseEntity<Nomination> createNomination(@RequestBody NominationCreateRequest request) {
        Nomination nomination = adminService.createNomination(request);
        return ResponseEntity.ok(nomination);
    }

    @PostMapping("/candidates")
    public ResponseEntity<Candidate> createCandidate(@RequestBody CandidateCreateRequest request) {
        Candidate candidate = adminService.createCandidate(request);
        return ResponseEntity.ok(candidate);
    }

    @PostMapping("/nominations/{nominationId}/candidates/{candidateId}")
    public ResponseEntity<?> addCandidateToNomination(@PathVariable Integer nominationId,
                                                      @PathVariable Integer candidateId) {
        adminService.addCandidateToNomination(candidateId, nominationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/nominations/{nominationId}/close")
    public ResponseEntity<?> closeNomination(@PathVariable Integer nominationId) {
        adminService.setNominationActive(nominationId, false);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/nominations/{nominationId}/open")
    public ResponseEntity<?> openNomination(@PathVariable Integer nominationId) {
        adminService.setNominationActive(nominationId, true);
        return ResponseEntity.ok().build();
    }
}
