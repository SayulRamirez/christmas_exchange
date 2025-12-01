package com.saul.christmas_exchange.repository;

import com.saul.christmas_exchange.entity.Exchange;
import com.saul.christmas_exchange.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {

    @Query("SELECT e.receiver FROM Exchange e WHERE e.giver.idParticipant = :idGiver")
    Optional<Participant> findReceiverByIdGiver(@Param("idGiver") Integer idGiver);
}
