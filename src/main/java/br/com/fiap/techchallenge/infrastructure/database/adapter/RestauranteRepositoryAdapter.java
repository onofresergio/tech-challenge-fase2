package br.com.fiap.techchallenge.infrastructure.database.adapter;

import br.com.fiap.techchallenge.domain.model.Restaurante;
import br.com.fiap.techchallenge.infrastructure.database.entity.RestauranteEntity;
import br.com.fiap.techchallenge.infrastructure.database.jpa.RestauranteJpaRepository;
import br.com.fiap.techchallenge.usecase.port.output.RestauranteRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RestauranteRepositoryAdapter implements RestauranteRepositoryPort {

    private final RestauranteJpaRepository restauranteJpaRepository;

    public RestauranteRepositoryAdapter(RestauranteJpaRepository repository) {
        this.restauranteJpaRepository = repository;
    }

    @Override
    public Restaurante save(Restaurante restaurante) {
        var entity = RestauranteEntity.fromDomain(restaurante);
        var savedEntity = restauranteJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Restaurante> findById(Long id) {
        return restauranteJpaRepository.findById(id).map(RestauranteEntity::toDomain);
    }

    @Override
    public List<Restaurante> findAll() {
        return restauranteJpaRepository.findAll().stream().map(RestauranteEntity::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        restauranteJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return restauranteJpaRepository.existsById(id);
    }
}
