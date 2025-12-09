package com.slay.groupAwards.repository;

import com.slay.groupAwards.repository.models.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
}
