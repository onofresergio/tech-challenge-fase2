package br.com.fiap.techchallenge.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BCryptPasswordHasherAdapterTest {

    @Test
    void shouldHashAndMatchPasswordUsingBCrypt() {
        var adapter = new BCryptPasswordHasherAdapter(new BCryptPasswordEncoder(12));

        var hashed = adapter.hash("Senha@123");

        assertNotEquals("Senha@123", hashed);
        assertTrue(hashed.startsWith("$2a$") || hashed.startsWith("$2b$") || hashed.startsWith("$2y$"));
        assertTrue(adapter.matches("Senha@123", hashed));
    }
}
