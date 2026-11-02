package br.com.fiap.techchallenge.infrastructure.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CriarRestauranteRequest(
        @Schema(description = "Nome do restaurante.", example = "Sabor do Norte")
        @NotBlank(message = "O nome é obrigatório.")
        String nome,

        @Schema(description = "Identificador do endereço do restaurante.", example = "1")
        @NotNull(message = "O endereço é obrigatório.")
        @Positive(message = "ID de endereço inválido.")
        Long enderecoId,

        @Schema(description = "Identificador do tipo de cozinha do restaurante.", example = "2")
        @NotNull(message = "O tipo de cozinha é obrigatório.")
        @Positive(message = "ID de tipo de cozinha inválido.")
        Long tipoCozinhaId,

        @Schema(description = "Horário de funcionamento do restaurante no formato ISO 8601.", example = "2025-10-11T12:00:00")
        @NotNull(message = "O horário de funcionamento é obrigatório.")
        LocalDateTime horarioFuncionamento,

        @Schema(description = "Identificador do usuário responsável pelo restaurante.", example = "3")
        @NotNull(message = "O usuário responsável é obrigatório.")
        @Positive(message = "ID de usuário inválido.")
        Long usuarioId
) {
}
