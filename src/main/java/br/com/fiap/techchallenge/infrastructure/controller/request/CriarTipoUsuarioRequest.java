package br.com.fiap.techchallenge.infrastructure.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CriarTipoUsuarioRequest(
        @Schema(description = "Nome do tipo de usuário.", example = "DONO_RESTAURANTE")
        @NotBlank(message = "O nome do tipo é obrigatório.")
        String nome
) {

}
