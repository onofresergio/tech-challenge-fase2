package br.com.fiap.techchallenge.usecase.port.output;

import br.com.fiap.techchallenge.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {
    Usuario save(Usuario usuario);
    List<Usuario> findAll();
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
    void deleteById(Long id);
    boolean existsById(Long id);
}
