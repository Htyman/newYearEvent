package com.slay.groupAwards.service;

import com.slay.groupAwards.repository.NominationRepository;
import com.slay.groupAwards.repository.UserRepository;
import com.slay.groupAwards.repository.VoteRepository;
import com.slay.groupAwards.repository.models.User;
import com.slay.groupAwards.service.dto.UserVoteStatusDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminReportService {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final NominationRepository nominationRepository;

    public AdminReportService(UserRepository userRepository,
                              VoteRepository voteRepository,
                              NominationRepository nominationRepository) {
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.nominationRepository = nominationRepository;
    }

    public List<UserVoteStatusDto> getUserVoteStatuses() {
        long totalNoms = nominationRepository.count();
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(u -> {
                    long voted = voteRepository.countVotedNominationsByUser(u.getId());
                    return new UserVoteStatusDto(u, voted, totalNoms);
                })
                .toList();
    }
}
