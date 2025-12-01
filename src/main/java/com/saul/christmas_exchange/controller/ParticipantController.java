package com.saul.christmas_exchange.controller;

import com.saul.christmas_exchange.model.ParticipantResponse;
import com.saul.christmas_exchange.service.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/participant")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/{giver}/options")
    public ResponseEntity<List<ParticipantResponse>> getAllUnselectedParticipants(@PathVariable int giver) {
        return ResponseEntity.ok(participantService.getAllUnselectedParticipants(giver));
    }

    @GetMapping("/{giver}/receiver")
    public ResponseEntity<ParticipantResponse> getReceiver(@PathVariable int giver) {
        return ResponseEntity.ok(participantService.getReceiver(giver));
    }

    @PostMapping("/{giver}/assign/{receiver}")
    public ResponseEntity<ParticipantResponse> selectedReceiver(@PathVariable int giver, @PathVariable int receiver) {
        return ResponseEntity.ok(participantService.selectedReceiver(giver, receiver));
    }

    @GetMapping("/assigned/{giver}")
    public ResponseEntity<Map<String, Boolean>> isAssignedReceiver(@PathVariable int giver) {
        return ResponseEntity.ok(participantService.alreadySelected(giver));
    }
}
