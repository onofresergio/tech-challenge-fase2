package br.com.fiap.techchallenge.infrastructure.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TipoUsuarioRequest(
        @Schema(description = "Identificador do tipo de usuário.", example = "2")
        @NotNull(message = "O ID é obrigatório.")
        @Positive(message = "O ID deve ser maior que zero.")
        Long id,

        @Schema(description = "Nome do tipo de usuário.", example = "DONO_RESTAURANTE")
        @NotBlank(message = "O nome do tipo é obrigatório.")
        String nome
) {

}
