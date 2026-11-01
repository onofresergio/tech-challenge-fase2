package br.com.fiap.techchallenge.infrastructure.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AtualizarMenuRequest(
        @Schema(description = "Nome do prato.", example = "Lasanha à Bolonhesa")
        @NotBlank(message = "O nome do prato é obrigatório.")
        String nome,

        @Schema(description = "Descrição detalhada do prato.", example = "Lasanha com molho bolonhesa e queijo gratinado.")
        @NotBlank(message = "A descrição do prato é obrigatória.")
        String descricao,

        @Schema(description = "Valor do prato em reais.", example = "39.90")
        @NotNull(message = "O valor do prato é obrigatório.")
        @Positive(message = "O valor do prato deve ser maior que zero.")
        BigDecimal valorPrato,

        @Schema(description = "Indica se o prato está disponível para venda no restaurante.", example = "true")
        @NotNull(message = "A disponibilidade do prato é obrigatória.")
        Boolean disponivelRestaurante,

        @Schema(description = "Caminho ou URL da foto do prato.", example = "https://img.example.com/lasanha.jpg")
        @NotBlank(message = "O caminho da foto do prato é obrigatório.")
        String fotoPrato
) {
}
