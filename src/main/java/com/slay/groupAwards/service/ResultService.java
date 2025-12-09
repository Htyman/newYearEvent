package com.slay.groupAwards.service;

import com.slay.groupAwards.repository.NominationRepository;
import com.slay.groupAwards.repository.VoteRepository;
import com.slay.groupAwards.repository.models.Nomination;
import com.slay.groupAwards.service.dto.CandidateResultDto;
import com.slay.groupAwards.service.dto.NominationResultsView;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {

    private final NominationRepository nominationRepository;
    private final VoteRepository voteRepository;

    public ResultService(NominationRepository nominationRepository,
                         VoteRepository voteRepository) {
        this.nominationRepository = nominationRepository;
        this.voteRepository = voteRepository;
    }

    /**
     * Итоги по всем закрытым номинациям (isActive = false).
     */
    public List<NominationResultsView> getClosedNominationResults() {
        List<Nomination> nominations = nominationRepository.findByActiveFalseOrderByNameAsc();

        return nominations.stream()
                .map(n -> {
                    List<CandidateResultDto> results =
                            voteRepository.findResultsForNomination(n.getId());
                    return new NominationResultsView(n, results);
                })
                .toList();
    }
}
