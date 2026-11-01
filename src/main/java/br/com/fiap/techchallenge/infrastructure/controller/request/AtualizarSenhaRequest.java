package br.com.fiap.techchallenge.infrastructure.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AtualizarSenhaRequest(
        @Schema(description = "Nova senha do usuário. Será convertida em hash BCrypt antes do armazenamento.", example = "NovaSenha@2025")
        @NotBlank(message = "A nova senha do usuário é obrigatória.")
        String novaSenha
) {
}
