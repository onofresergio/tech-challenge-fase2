package br.com.fiap.techchallenge.usecase.port.input;

import br.com.fiap.techchallenge.domain.model.Usuario;

import java.util.List;

public interface BuscarUsuarioUseCase {
    Usuario findById(Long id);
    List<Usuario> findAll();
    List<Usuario> findByNomeContaining(String nome);
    boolean existsById(Long id);
}
