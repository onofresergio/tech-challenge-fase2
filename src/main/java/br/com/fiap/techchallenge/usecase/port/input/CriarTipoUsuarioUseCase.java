package br.com.fiap.techchallenge.usecase.port.input;

import br.com.fiap.techchallenge.domain.model.TipoUsuario;

public interface CriarTipoUsuarioUseCase {
    TipoUsuario create(String nome);
}
