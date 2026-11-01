package br.com.fiap.techchallenge.infrastructure.database.adapter;

import br.com.fiap.techchallenge.domain.model.Usuario;
import br.com.fiap.techchallenge.infrastructure.database.entity.UsuarioEntity;
import br.com.fiap.techchallenge.infrastructure.database.jpa.UsuarioJpaRepository;
import br.com.fiap.techchallenge.usecase.port.output.PasswordHasherPort;
import br.com.fiap.techchallenge.usecase.port.output.UsuarioRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository repository;

    @Autowired
    public UsuarioRepositoryAdapter(UsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Deprecated
    public UsuarioRepositoryAdapter(UsuarioJpaRepository repository, PasswordHasherPort ignoredPasswordHasherPort) {
        this(repository);
    }

    @Override
    public Usuario save(Usuario usuario) {
        var entity = UsuarioEntity.fromDomain(usuario);
        return repository.save(entity).toDomain();
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll().stream().map(UsuarioEntity::toDomain).toList();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return repository.findById(id).map(UsuarioEntity::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return repository.findByEmail(email).map(UsuarioEntity::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
