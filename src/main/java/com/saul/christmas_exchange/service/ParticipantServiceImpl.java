package com.saul.christmas_exchange.service;

import com.saul.christmas_exchange.entity.Exchange;
import com.saul.christmas_exchange.entity.Participant;
import com.saul.christmas_exchange.model.ParticipantResponse;
import com.saul.christmas_exchange.repository.ExchangeRepository;
import com.saul.christmas_exchange.repository.ParticipantRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Transactional
    @Override
    public ParticipantResponse selectedReceiver(int giver, int receiver) {

        if (participantRepository.countBySelectedIsFalse(giver) == 2) {

            Optional<Participant> participant = exchangeRepository.findReceiverByIdGiver(receiver);

            if (participant.isPresent()) {
                if (participant.get().getIdParticipant() == giver) {
                    final int receiverSearch = receiver;

                    Participant newReceiver = participantRepository.findAllBySelectedIsFalseAndIdParticipantIsNot(giver)
                            .stream()
                            .filter(p -> p.getIdParticipant() != receiverSearch)
                            .findFirst()
                            .orElseThrow();

                    receiver = newReceiver.getIdParticipant();
                }
            }

        }

        Participant giverEntity = participantRepository.findById(giver)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró al participante con id: " + giver));

        Participant receiverEntity = participantRepository.findById(receiver)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró al participante"));

        if (receiverEntity.isSelected()) throw new EntityExistsException("Ya se selecciono");

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
