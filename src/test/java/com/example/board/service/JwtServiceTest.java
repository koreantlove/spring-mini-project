package com.example.board.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void JWT_생성_및_검증() {

        String username = "test1";

        String token = jwtService.createToken(username);

        System.out.println("JWT = " + token);

        assertNotNull(token);
        assertTrue(jwtService.isValid(token));

        String result = jwtService.getUsername(token);

        assertEquals(username, result);
    }
}