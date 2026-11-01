package br.com.fiap.techchallenge.infrastructure.controller.response;

import java.time.LocalDateTime;

public record RestauranteResponse(
        Long id,
        String nome,
        LocalDateTime horarioFuncionamento,
        EnderecoResponse endereco,
        TipoCozinhaResponse tipoCozinha,
        TipoUsuarioResponse tipoUsuario
) {
}
