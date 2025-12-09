package com.slay.groupAwards.service;

import com.slay.groupAwards.controller.dto.CandidateCreateRequest;
import com.slay.groupAwards.controller.dto.NominationCreateRequest;
import com.slay.groupAwards.exception.NotFoundException;
import com.slay.groupAwards.repository.CandidateInNominationRepository;
import com.slay.groupAwards.repository.CandidateRepository;
import com.slay.groupAwards.repository.NominationRepository;
import com.slay.groupAwards.repository.models.Candidate;
import com.slay.groupAwards.repository.models.CandidateInNomination;
import com.slay.groupAwards.repository.models.Nomination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final NominationRepository nominationRepository;
    private final CandidateRepository candidateRepository;
    private final CandidateInNominationRepository candidateInNominationRepository;

    public AdminService(NominationRepository nominationRepository,
                        CandidateRepository candidateRepository,
                        CandidateInNominationRepository candidateInNominationRepository) {
        this.nominationRepository = nominationRepository;
        this.candidateRepository = candidateRepository;
        this.candidateInNominationRepository = candidateInNominationRepository;
    }

    @Transactional
    public Nomination createNomination(NominationCreateRequest request) {
        Nomination nomination = new Nomination();
        nomination.setName(request.getName());
        nomination.setDescription(request.getDescription());
        nomination.setPhotoUrl(request.getPhotoUrl());
        nomination.setActive(true);
        return nominationRepository.save(nomination);
    }

    @Transactional
    public Candidate createCandidate(CandidateCreateRequest request) {
        Candidate candidate = new Candidate();
        candidate.setName(request.getName());
        candidate.setSurname(request.getSurname());
        candidate.setPhotoUrl(request.getPhotoUrl());
        candidate.setDescription(request.getDescription());
        return candidateRepository.save(candidate);
    }

    @Transactional
    public void addCandidateToNomination(Integer candidateId, Integer nominationId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate not found: " + candidateId));

        Nomination nomination = nominationRepository.findById(nominationId)
                .orElseThrow(() -> new NotFoundException("Nomination not found: " + nominationId));

        boolean exists = candidateInNominationRepository
                .findByCandidate_IdAndNomination_Id(candidateId, nominationId)
                .isPresent();

        if (!exists) {
            CandidateInNomination link = new CandidateInNomination(candidate, nomination);
            candidateInNominationRepository.save(link);
        }
    }

    @Transactional
    public void setNominationActive(Integer nominationId, boolean active) {
        Nomination nomination = nominationRepository.findById(nominationId)
                .orElseThrow(() -> new NotFoundException("Nomination not found: " + nominationId));
        nomination.setActive(active);
        nominationRepository.save(nomination);
    }
}
