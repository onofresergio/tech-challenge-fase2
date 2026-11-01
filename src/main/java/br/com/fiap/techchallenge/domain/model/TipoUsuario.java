package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.exception.TipoUsuarioValidationException;

public record TipoUsuario(Long id, String nome) {
    public TipoUsuario {
        if (id != null && id <= 0) {
            throw new TipoUsuarioValidationException("O ID do tipo de usuário deve ser maior que zero.");
        }
        if (nome == null || nome.isBlank()) {
            throw new TipoUsuarioValidationException("O nome do tipo de usuário não pode ser vazio.");
        }
        nome = nome.trim();
    }
}
