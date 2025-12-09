package com.slay.groupAwards.repository;

import com.slay.groupAwards.repository.models.Nomination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NominationRepository extends JpaRepository<Nomination, Integer> {

    List<Nomination> findByActiveTrue();
    List<Nomination> findByActiveFalseOrderByNameAsc();
}
