package br.com.fiap.techchallenge.infrastructure.database.adapter;

import br.com.fiap.techchallenge.domain.model.TipoUsuario;
import br.com.fiap.techchallenge.infrastructure.database.entity.TipoUsuarioEntity;
import br.com.fiap.techchallenge.infrastructure.database.jpa.TipoUsuarioJpaRepository;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TipoUsuarioRepositoryAdapter implements TipoUsuarioRepositoryPort {

    private final TipoUsuarioJpaRepository repository;

    public TipoUsuarioRepositoryAdapter(TipoUsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public TipoUsuario save(TipoUsuario userType) {
        var entity = TipoUsuarioEntity.fromDomain(userType);
        return repository.save(entity).toDomain();
    }

    @Override
    public Optional<TipoUsuario> findById(Long id) {
        return repository.findById(id).map(TipoUsuarioEntity::toDomain);
    }

    @Override
    public List<TipoUsuario> findAll() {
        return repository.findAll().stream().map(TipoUsuarioEntity::toDomain).toList();
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
