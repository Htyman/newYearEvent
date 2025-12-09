package com.slay.groupAwards.service;

import com.slay.groupAwards.controller.dto.CandidateDto;
import com.slay.groupAwards.controller.dto.CandidateResultDto;
import com.slay.groupAwards.exception.NotFoundException;
import com.slay.groupAwards.repository.CandidateInNominationRepository;
import com.slay.groupAwards.repository.NominationRepository;
import com.slay.groupAwards.repository.VoteRepository;
import com.slay.groupAwards.repository.models.CandidateInNomination;
import com.slay.groupAwards.repository.models.Nomination;
import com.slay.groupAwards.repository.projection.CandidateResultProjection;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NominationService {

    private final NominationRepository nominationRepository;
    private final CandidateInNominationRepository candidateInNominationRepository;
    private final VoteRepository voteRepository;

    public NominationService(NominationRepository nominationRepository,
                             CandidateInNominationRepository candidateInNominationRepository,
                             VoteRepository voteRepository) {
        this.nominationRepository = nominationRepository;
        this.candidateInNominationRepository = candidateInNominationRepository;
        this.voteRepository = voteRepository;
    }

    public List<Nomination> getActiveNominations() {
        return nominationRepository.findByActiveTrue();
    }

    public List<CandidateDto> getCandidatesForNomination(Integer nominationId) {
        Nomination nomination = nominationRepository.findById(nominationId)
                .orElseThrow(() -> new NotFoundException("Nomination not found: " + nominationId));

        List<CandidateInNomination> links =
                candidateInNominationRepository.findAllByNomination_Id(nomination.getId());

        return links.stream()
                .map(link -> {
                    var c = link.getCandidate();
                    return new CandidateDto(
                            c.getId(),
                            c.getName(),
                            c.getSurname(),
                            c.getPhotoUrl(),
                            c.getDescription()
                    );
                })
                .collect(Collectors.toList());
    }

    public List<CandidateResultDto> getResultsForNomination(Integer nominationId) {
        Nomination nomination = getNominationById(nominationId);

        // если номинация ещё активна — юзерам не показываем
        if (nomination.isActive()) {
            throw new IllegalStateException("Voting for this nomination is still open");
        }

        List<CandidateResultProjection> projections =
                voteRepository.getResultsForNomination(nominationId);

        return projections.stream()
                .map(p -> new CandidateResultDto(
                        p.getCandidateId(),
                        p.getName(),
                        p.getSurname(),
                        p.getVotes()
                ))
                .toList();
    }

    public Nomination getNominationById(Integer id) {
        return nominationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nomination not found: " + id));
    }

    public List<Nomination> getAllNominations() {
        return nominationRepository.findAll();
    }


}
