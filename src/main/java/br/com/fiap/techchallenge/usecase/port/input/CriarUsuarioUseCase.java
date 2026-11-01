package br.com.fiap.techchallenge.usecase.port.input;

import br.com.fiap.techchallenge.domain.model.Usuario;

public interface CriarUsuarioUseCase {
    Usuario create(String nome, String email, String senha, String login, Long enderecoId, Long tipoUsuarioId);
}
