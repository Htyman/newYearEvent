package com.slay.groupAwards.service;

import com.slay.groupAwards.exception.AlreadyVotedException;
import com.slay.groupAwards.exception.NotFoundException;
import com.slay.groupAwards.repository.*;
import com.slay.groupAwards.repository.models.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteService {

    private final UserRepository userRepository;
    private final NominationRepository nominationRepository;
    private final CandidateInNominationRepository candidateInNominationRepository;
    private final VoteRepository voteRepository;

    public VoteService(UserRepository userRepository,
                       NominationRepository nominationRepository,
                       CandidateInNominationRepository candidateInNominationRepository,
                       VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.nominationRepository = nominationRepository;
        this.candidateInNominationRepository = candidateInNominationRepository;
        this.voteRepository = voteRepository;
    }

    @Transactional
    public void vote(Integer nominationId, Integer candidateId) {
        // логин текущего пользователя из контекста безопасности
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("User not found by login: " + login));

        Nomination nomination = nominationRepository.findById(nominationId)
                .orElseThrow(() -> new NotFoundException("Nomination not found: " + nominationId));

        if (!nomination.isActive()) {
            throw new IllegalStateException("Voting in this nomination is closed");
        }

        CandidateInNomination candidateInNomination = candidateInNominationRepository
                .findByCandidate_IdAndNomination_Id(candidateId, nominationId)
                .orElseThrow(() -> new NotFoundException(
                        "Candidate " + candidateId + " is not in nomination " + nominationId
                ));

        boolean alreadyVoted = voteRepository.hasUserVotedInNomination(user.getId(), nominationId);
        if (alreadyVoted) {
            throw new AlreadyVotedException("User " + user.getId() + " has already voted in nomination " + nominationId);
        }

        Vote vote = new Vote(user, candidateInNomination);
        voteRepository.save(vote);
    }

    public Optional<Vote> getUserVoteForNomination(Integer nominationId) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("User not found: " + login));

        return voteRepository.findByUser_IdAndCandidateInNomination_Nomination_Id(user.getId(), nominationId);
    }

    @Transactional
    public void cancelVote(Integer nominationId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
                "anonymousUser".equals(auth.getPrincipal())) {
            throw new AccessDeniedException("Not authenticated");
        }

        String login = auth.getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("User not found: " + login));

        voteRepository.deleteByUserAndNomination(user.getId(), nominationId);
        // если хочешь, можешь проверить, что удалили >= 1 записи и, если 0, кидать NotFoundException
    }

    // временная функция для генерации хеша пароля, чисто тестануть
//    public static void main(String[] args) {
//        System.out.println(new BCryptPasswordEncoder().encode("password123"));
//    }

}
