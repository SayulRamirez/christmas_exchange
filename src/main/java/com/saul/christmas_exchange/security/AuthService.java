package com.saul.christmas_exchange.security;

import com.saul.christmas_exchange.entity.Participant;
import com.saul.christmas_exchange.model.AuthResponse;
import com.saul.christmas_exchange.model.LoginRequest;
import com.saul.christmas_exchange.model.RegisterRequest;
import com.saul.christmas_exchange.repository.ParticipantRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Map;

@Service
public class AuthService {

    private final ParticipantRepository participantRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthService(ParticipantRepository participantRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.participantRepository = participantRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        Participant user = participantRepository.findByUsername(request.username())
                .orElseThrow(() -> new EntityNotFoundException("no se encontro el usuario"));

        String token = jwtService.getToken(user);

        return new AuthResponse(token, user.getIdParticipant());
    }

    public void register(RegisterRequest request) {
        if (participantRepository.existsByUsername(request.username())) throw new EntityExistsException("El usuario ya existe");

        participantRepository.save(new Participant(null,
                request.username(),
                passwordEncoder.encode(request.password()),
                false));

    }

    public Map<String, Boolean> status() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Mexico_City"));

        ZonedDateTime startLogin = ZonedDateTime.of(
                2025, 12, 4, 9, 0, 0, 0,
                ZoneId.of("America/Mexico_City")
        );

        boolean loginEnabled = now.isAfter(startLogin);

        return Map.of("status", loginEnabled);
    }
}
