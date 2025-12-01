package com.saul.christmas_exchange.model;

public record RegisterRequest(
        String name,
        String username,
        String password
) {
}
