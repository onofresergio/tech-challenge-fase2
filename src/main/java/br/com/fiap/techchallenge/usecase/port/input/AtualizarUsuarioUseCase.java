package br.com.fiap.techchallenge.usecase.port.input;

import br.com.fiap.techchallenge.domain.model.Usuario;

public interface AtualizarUsuarioUseCase {
    Usuario update(Long id, String nome, String email, String login, Long enderecoId, Long tipoUsuarioId);
}
