package br.com.fiap.techchallenge.usecase.port.output;

public interface PasswordHasherPort {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
