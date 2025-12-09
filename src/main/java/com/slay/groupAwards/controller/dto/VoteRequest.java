package com.slay.groupAwards.controller.dto;

public class VoteRequest {

    private Integer nominationId;
    private Integer candidateId;

    public VoteRequest() {
    }

    public Integer getNominationId() {
        return nominationId;
    }

    public void setNominationId(Integer nominationId) {
        this.nominationId = nominationId;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }
}
