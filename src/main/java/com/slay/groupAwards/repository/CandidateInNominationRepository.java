package com.slay.groupAwards.repository;

import com.slay.groupAwards.repository.models.CandidateInNomination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateInNominationRepository
        extends JpaRepository<CandidateInNomination, Integer> {

    Optional<CandidateInNomination> findByCandidate_IdAndNomination_Id(Integer candidateId, Integer nominationId);

    List<CandidateInNomination> findAllByNomination_Id(Integer nominationId);
}
