package com.slay.groupAwards.repository.projection;

public interface CandidateResultProjection {

    Integer getCandidateId();
    String getName();
    String getSurname();
    Long getVotes();
}
