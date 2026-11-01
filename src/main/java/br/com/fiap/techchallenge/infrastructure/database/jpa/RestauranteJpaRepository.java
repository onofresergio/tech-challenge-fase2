package br.com.fiap.techchallenge.infrastructure.database.jpa;

import br.com.fiap.techchallenge.domain.model.Restaurante;
import br.com.fiap.techchallenge.infrastructure.database.entity.RestauranteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

public interface RestauranteJpaRepository extends ListCrudRepository<RestauranteEntity, Long> {

}
