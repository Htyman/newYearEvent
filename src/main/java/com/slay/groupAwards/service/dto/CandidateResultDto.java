package com.slay.groupAwards.service.dto;

public class CandidateResultDto {
    private final Integer candidateId;
    private final String name;
    private final String surname;
    private final long votes;

    public CandidateResultDto(Integer candidateId, String name, String surname, long votes) {
        this.candidateId = candidateId;
        this.name = name;
        this.surname = surname;
        this.votes = votes;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public long getVotes() {
        return votes;
    }
}
