package br.com.fiap.techchallenge.infrastructure.controller.response;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String login,
        EnderecoResponse endereco,
        TipoUsuarioResponse tipoUsuario
) {
}
