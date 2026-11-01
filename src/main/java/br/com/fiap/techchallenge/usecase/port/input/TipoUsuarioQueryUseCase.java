package br.com.fiap.techchallenge.usecase.port.input;

import br.com.fiap.techchallenge.domain.model.TipoUsuario;

import java.util.List;

public interface TipoUsuarioQueryUseCase {
    TipoUsuario findById(Long id);
    List<TipoUsuario> findAll();
    boolean existsById(Long id);
}
