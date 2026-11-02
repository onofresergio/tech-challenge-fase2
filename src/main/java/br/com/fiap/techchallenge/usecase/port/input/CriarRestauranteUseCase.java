package br.com.fiap.techchallenge.usecase.port.input;

import br.com.fiap.techchallenge.domain.model.Restaurante;

import java.time.LocalDateTime;

public interface CriarRestauranteUseCase {
    Restaurante execute(String nome, Long enderecoId, Long tipoCozinhaId, LocalDateTime horarioFuncionamento, Long usuarioId);
}
