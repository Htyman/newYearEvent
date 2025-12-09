package com.slay.groupAwards.repository.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_in_nomination_id", nullable = false)
    private CandidateInNomination candidateInNomination;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Vote() {
    }

    public Vote(User user, CandidateInNomination candidateInNomination) {
        this.user = user;
        this.candidateInNomination = candidateInNomination;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public CandidateInNomination getCandidateInNomination() {
        return candidateInNomination;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCandidateInNomination(CandidateInNomination candidateInNomination) {
        this.candidateInNomination = candidateInNomination;
    }
}
