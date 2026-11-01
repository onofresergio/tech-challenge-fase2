package br.com.fiap.techchallenge.infrastructure.database.adapter;

import br.com.fiap.techchallenge.domain.model.Endereco;
import br.com.fiap.techchallenge.infrastructure.database.entity.EnderecoEntity;
import br.com.fiap.techchallenge.infrastructure.database.jpa.EnderecoJpaRepository;
import br.com.fiap.techchallenge.usecase.port.output.EnderecoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EnderecoRepositoryAdapter implements EnderecoRepositoryPort {

    private final EnderecoJpaRepository repository;

    public EnderecoRepositoryAdapter(EnderecoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Endereco save(Endereco endereco) {
        var entity = EnderecoEntity.fromDomain(endereco);
        return repository.save(entity).toDomain();
    }

    @Override
    public List<Endereco> findAll() {
        return repository.findAll().stream().map(EnderecoEntity::toDomain).toList();
    }

    @Override
    public Optional<Endereco> findById(Long id) {
        return repository.findById(id).map(EnderecoEntity::toDomain);
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
