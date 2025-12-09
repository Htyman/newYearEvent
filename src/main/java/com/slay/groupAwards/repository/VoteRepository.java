package com.slay.groupAwards.repository;

import com.slay.groupAwards.repository.models.Vote;
import com.slay.groupAwards.repository.projection.CandidateResultProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.slay.groupAwards.service.dto.CandidateResultDto;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Query("""
        select new com.slay.groupAwards.service.dto.CandidateResultDto(
            c.id,
            c.name,
            c.surname,
            count(v.id)
        )
        from CandidateInNomination cin
        join cin.candidate c
        left join Vote v on v.candidateInNomination = cin
        where cin.nomination.id = :nominationId
        group by c.id, c.name, c.surname
        order by count(v.id) desc, c.name asc
        """)
    List<CandidateResultDto> findResultsForNomination(@Param("nominationId") Integer nominationId);

    @Query("""
        select count(distinct v.candidateInNomination.nomination.id)
        from Vote v
        where v.user.id = :userId
        """)
    long countVotedNominationsByUser(@Param("userId") Integer userId);


    @Query("""
            SELECT CASE WHEN COUNT(v) > 0 THEN TRUE ELSE FALSE END
            FROM Vote v
            WHERE v.user.id = :userId
              AND v.candidateInNomination.nomination.id = :nominationId
            """)
    boolean hasUserVotedInNomination(@Param("userId") Integer userId,
                                     @Param("nominationId") Integer nominationId);

    @Query("""
            SELECT v.candidateInNomination.candidate.id AS candidateId,
                   v.candidateInNomination.candidate.name AS name,
                   v.candidateInNomination.candidate.surname AS surname,
                   COUNT(v) AS votes
            FROM Vote v
            WHERE v.candidateInNomination.nomination.id = :nominationId
            GROUP BY v.candidateInNomination.candidate.id,
                     v.candidateInNomination.candidate.name,
                     v.candidateInNomination.candidate.surname
            ORDER BY votes DESC
            """)
    List<CandidateResultProjection> getResultsForNomination(@Param("nominationId") Integer nominationId);

    // найти голос конкретного юзера в конкретной номинации
    Optional<Vote> findByUser_IdAndCandidateInNomination_Nomination_Id(Integer userId, Integer nominationId);

    // удалить голос юзера по номинации
    @Modifying
    @Query("delete from Vote v where v.user.id = :userId and v.candidateInNomination.nomination.id = :nominationId")
    int deleteByUserAndNomination(@Param("userId") Integer userId,
                                  @Param("nominationId") Integer nominationId);
}
