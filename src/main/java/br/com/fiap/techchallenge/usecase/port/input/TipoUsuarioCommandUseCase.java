package br.com.fiap.techchallenge.usecase.port.input;

import br.com.fiap.techchallenge.domain.model.TipoUsuario;

public interface TipoUsuarioCommandUseCase {
    TipoUsuario update(Long id, String nome);
    void delete(Long id);
}
