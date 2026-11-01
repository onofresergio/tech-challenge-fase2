package br.com.fiap.techchallenge.infrastructure.database.adapter;

import br.com.fiap.techchallenge.domain.model.TipoCozinha;
import br.com.fiap.techchallenge.infrastructure.database.entity.TipoCozinhaEntity;
import br.com.fiap.techchallenge.infrastructure.database.jpa.TipoCozinhaJpaRepository;
import br.com.fiap.techchallenge.usecase.port.output.TipoCozinhaRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TipoCozinhaRepositoryAdapter implements TipoCozinhaRepositoryPort {

    private final TipoCozinhaJpaRepository repository;

    public TipoCozinhaRepositoryAdapter(TipoCozinhaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public TipoCozinha save(TipoCozinha tipoCozinha) {
        var entity = TipoCozinhaEntity.fromDomain(tipoCozinha);
        return repository.save(entity).toDomain();
    }

    @Override
    public Optional<TipoCozinha> findById(Long id) {
        return repository.findById(id).map(TipoCozinhaEntity::toDomain);
    }

    @Override
    public List<TipoCozinha> findAll() {
        return repository.findAll().stream().map(TipoCozinhaEntity::toDomain).toList();
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
