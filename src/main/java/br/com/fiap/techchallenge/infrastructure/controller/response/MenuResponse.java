package br.com.fiap.techchallenge.infrastructure.controller.response;

import java.math.BigDecimal;

public record MenuResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal valorPrato,
        boolean disponivelRestaurante,
        String fotoPrato,
        RestauranteResponse restaurante
) {
}
