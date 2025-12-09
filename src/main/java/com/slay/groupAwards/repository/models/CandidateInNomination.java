package com.slay.groupAwards.repository.models;

import jakarta.persistence.*;

@Entity
@Table(
        name = "candidate_in_nomination",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "ux_candidate_nomination",
                        columnNames = {"candidates_id", "nominations_id"}
                )
        }
)
public class CandidateInNomination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidates_id", nullable = false)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nominations_id", nullable = false)
    private Nomination nomination;

    public CandidateInNomination() {
    }

    public CandidateInNomination(Candidate candidate, Nomination nomination) {
        this.candidate = candidate;
        this.nomination = nomination;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Nomination getNomination() {
        return nomination;
    }

    public void setNomination(Nomination nomination) {
        this.nomination = nomination;
    }
}
