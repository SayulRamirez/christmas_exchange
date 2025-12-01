package com.saul.christmas_exchange.repository;

import com.saul.christmas_exchange.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

    List<Participant> findAllBySelectedIsFalseAndIdParticipantIsNot(Integer giver);

    Optional<Participant> findByUsername(String username);

    boolean existsByUsername(String username);
}
