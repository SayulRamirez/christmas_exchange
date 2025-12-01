package com.saul.christmas_exchange.service;

import com.saul.christmas_exchange.entity.Exchange;
import com.saul.christmas_exchange.entity.Participant;
import com.saul.christmas_exchange.model.ParticipantResponse;
import com.saul.christmas_exchange.repository.ExchangeRepository;
import com.saul.christmas_exchange.repository.ParticipantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;

    private final ExchangeRepository exchangeRepository;

    public ParticipantServiceImpl(ParticipantRepository participantRepository, ExchangeRepository exchangeRepository) {
        this.participantRepository = participantRepository;
        this.exchangeRepository = exchangeRepository;
    }

    @Override
    public List<ParticipantResponse> getAllUnselectedParticipants(int giver) {

        return participantRepository.findAllBySelectedIsFalseAndIdParticipantIsNot(giver)
                .stream().map(this::toResponse)
                .toList();
    }

    @Override
    public ParticipantResponse getReceiver(int giver) {
        Participant receiver = exchangeRepository.findReceiverByIdGiver(giver)
                .orElseThrow(() -> new EntityNotFoundException("no se encontró a quien regalara el id: " + giver));

        return toResponse(receiver);
    }

    @Override
    public ParticipantResponse selectedReceiver(int giver, int receiver) {

        Participant giverEntity = participantRepository.findById(giver)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró al participante con id: " + giver));

        Participant receiverEntity = participantRepository.findById(receiver)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró al participante con id: " + receiver));

        giverEntity.setAssigned(true);

        receiverEntity.setSelected(true);

        participantRepository.saveAll(List.of(giverEntity, receiverEntity));

        exchangeRepository.save(new Exchange(null, giverEntity, receiverEntity));

        return toResponse(receiverEntity);
    }

    @Override
    public Map<String, Boolean> alreadySelected(int giver) {
        Participant participant = participantRepository.findById(giver)
                .orElseThrow(() -> new EntityNotFoundException("no se encontro al usuario"));

        return Map.of("assigned", participant.isAssigned());
    }

    private ParticipantResponse toResponse(Participant entity) {
        return new ParticipantResponse(entity.getIdParticipant(), entity.getName());
    }
}
