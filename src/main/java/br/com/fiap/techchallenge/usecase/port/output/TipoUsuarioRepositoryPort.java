package br.com.fiap.techchallenge.usecase.port.output;

import br.com.fiap.techchallenge.domain.model.TipoUsuario;

import java.util.List;
import java.util.Optional;

public interface TipoUsuarioRepositoryPort {
    TipoUsuario save(TipoUsuario tipoUsuario);
    List<TipoUsuario> findAll();
    Optional<TipoUsuario> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);
}
