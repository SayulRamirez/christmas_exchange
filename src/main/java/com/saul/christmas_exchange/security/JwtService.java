package com.saul.christmas_exchange.security;

import com.saul.christmas_exchange.entity.Participant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value(value = "${secret.key}")
    private String SECRET_KEY;

    public String getToken(Participant user) {
        return getToken(new HashMap<>(), user);
    }

    private String getToken(Map<String, Object> extraClaims, Participant user) {

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Mexico_City"));
        return Jwts.builder()
                .setClaims(extraClaims)
                .setId(user.getIdParticipant().toString())
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(now.plusHours(2L).toInstant()))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String getEmailFromToken(String token) {
        return getClaims(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);

        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date getExpiration(String token) {
        return getClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(Date.from(ZonedDateTime.now(ZoneId.of("America/Mexico_City")).toInstant()));
    }
}





