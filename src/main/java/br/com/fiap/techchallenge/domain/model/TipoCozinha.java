package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.exception.TipoCozinhaValidationException;

public record TipoCozinha(Long id, String nome) {
    public TipoCozinha {
        if (id == null || id <= 0) {
            throw new TipoCozinhaValidationException("O ID do tipo de cozinha deve ser um número positivo.");
        }
        if (nome == null || nome.isBlank()) {
            throw new TipoCozinhaValidationException("O nome do tipo de cozinha não pode ser vazio.");
        }
        nome = nome.trim();
    }
}
