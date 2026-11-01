package br.com.fiap.techchallenge.infrastructure.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UsuarioUpdateRequest(
        @Schema(description = "Nome completo do usuário.", example = "Maria Souza")
        @NotBlank(message = "O nome do usuário é obrigatório.")
        String nome,

        @Schema(description = "E-mail do usuário.", example = "maria@email.com")
        @NotBlank(message = "O e-mail do usuário é obrigatório.")
        String email,

        @Schema(description = "Login do usuário.", example = "maria.souza")
        @NotBlank(message = "O login do usuário é obrigatório.")
        String login,

        @Schema(description = "Identificador do endereço do usuário.", example = "1")
        @NotNull(message = "O ID do endereço é obrigatório.")
        @Positive(message = "ID de endereço inválido.")
        Long enderecoId,

        @Schema(description = "Identificador do tipo de usuário.", example = "2")
        @NotNull(message = "O ID do tipo de usuário é obrigatório.")
        @Positive(message = "ID de tipo de usuário inválido.")
        Long tipoUsuarioId
) {
}
