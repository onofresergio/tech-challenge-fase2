package br.com.fiap.techchallenge.domain.exception;

public class MenuNotFoundException extends BusinessException {
    public MenuNotFoundException(Long id) {
        super("Menu com ID " + id + " não foi encontrado.");
    }
}
