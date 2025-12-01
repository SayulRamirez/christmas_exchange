package com.saul.christmas_exchange.service;

import com.saul.christmas_exchange.model.ParticipantResponse;

import java.util.List;
import java.util.Map;

public interface ParticipantService {

    List<ParticipantResponse> getAllUnselectedParticipants(int giver);

    ParticipantResponse getReceiver(int giver);

    ParticipantResponse selectedReceiver(int giver, int receiver);

    Map<String, Boolean> alreadySelected(int giver);
}
