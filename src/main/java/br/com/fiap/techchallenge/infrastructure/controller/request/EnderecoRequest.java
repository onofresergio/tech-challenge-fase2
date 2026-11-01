package br.com.fiap.techchallenge.infrastructure.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record EnderecoRequest(
        @Schema(description = "Nome da rua do endereço.", example = "Avenida Paulista")
        @NotBlank(message = "A rua do endereço é obrigatória.")
        String rua,

        @Schema(description = "Número do endereço.", example = "1500")
        @NotBlank(message = "O número do endereço é obrigatório.")
        String numero,

        @Schema(description = "Cidade do endereço.", example = "São Paulo")
        @NotBlank(message = "A cidade do endereço é obrigatória.")
        String cidade,

        @Schema(description = "Estado do endereço.", example = "SP")
        @NotBlank(message = "O estado do endereço é obrigatório.")
        String estado,

        @Schema(description = "CEP do endereço.", example = "01310-100")
        @NotBlank(message = "O CEP do endereço é obrigatório.")
        String cep
) {
}
