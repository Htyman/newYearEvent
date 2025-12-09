package com.slay.groupAwards.service.dto;

import com.slay.groupAwards.repository.models.Nomination;

import java.util.List;

public class NominationResultsView {

    private final Nomination nomination;
    private final List<CandidateResultDto> results;

    public NominationResultsView(Nomination nomination, List<CandidateResultDto> results) {
        this.nomination = nomination;
        this.results = results;
    }

    public Nomination getNomination() {
        return nomination;
    }

    public List<CandidateResultDto> getResults() {
        return results;
    }

    public CandidateResultDto getWinner() {
        return results.isEmpty() ? null : results.get(0);
    }
}
