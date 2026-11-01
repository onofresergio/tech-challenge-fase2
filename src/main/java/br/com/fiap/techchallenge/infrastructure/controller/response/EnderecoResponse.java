package br.com.fiap.techchallenge.infrastructure.controller.response;

public record EnderecoResponse(
        Long id,
        String rua,
        String numero,
        String cidade,
        String estado,
        String cep
) {
}
