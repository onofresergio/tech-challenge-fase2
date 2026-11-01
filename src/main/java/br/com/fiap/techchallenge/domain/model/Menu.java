package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.exception.MenuValidationException;

import java.math.BigDecimal;

public record Menu(
        Long id,
        String nome,
        String descricao,
        BigDecimal valorPrato,
        boolean disponivelRestaurante,
        String fotoPrato,
        Long restauranteId
) {
    public Menu {
        if (id != null && id <= 0) {
            throw new MenuValidationException("O ID do menu deve ser maior que zero.");
        }
        nome = nome == null ? null : nome.trim();
        descricao = descricao == null ? null : descricao.trim();
        fotoPrato = fotoPrato == null ? null : fotoPrato.trim();

        if (nome == null || nome.isBlank()) {
            throw new MenuValidationException("O nome do prato é obrigatório.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new MenuValidationException("A descrição do prato é obrigatória.");
        }
        if (valorPrato == null || valorPrato.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MenuValidationException("O valor do prato deve ser maior que zero.");
        }
        if (fotoPrato == null || fotoPrato.isBlank()) {
            throw new MenuValidationException("O caminho da foto do prato é obrigatório.");
        }
        if (restauranteId == null || restauranteId <= 0) {
            throw new MenuValidationException("ID do restaurante inválido.");
        }
    }
}
