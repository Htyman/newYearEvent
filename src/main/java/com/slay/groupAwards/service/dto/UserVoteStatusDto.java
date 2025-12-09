package com.slay.groupAwards.service.dto;

import com.slay.groupAwards.repository.models.User;

public class UserVoteStatusDto {

    private final User user;
    private final long votedNominations;
    private final long totalNominations;

    public UserVoteStatusDto(User user, long votedNominations, long totalNominations) {
        this.user = user;
        this.votedNominations = votedNominations;
        this.totalNominations = totalNominations;
    }

    public User getUser() {
        return user;
    }

    public long getVotedNominations() {
        return votedNominations;
    }

    public long getTotalNominations() {
        return totalNominations;
    }

    public boolean isCompleted() {
        return totalNominations > 0 && votedNominations == totalNominations;
    }
}
