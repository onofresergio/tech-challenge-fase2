package br.com.fiap.techchallenge.usecase.port.input;

import br.com.fiap.techchallenge.domain.model.Restaurante;

import java.time.LocalDateTime;

public interface UpdateRestauranteUseCase {
    Restaurante execute(Long id, String nome, Long enderecoId, Long tipoCozinhaId, LocalDateTime horarioFuncionamento, Long tipoUsuarioId);
}
