package com.slay.groupAwards.controller.dto;

public class CandidateResultDto {

    private Integer candidateId;
    private String name;
    private String surname;
    private long votes;

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
