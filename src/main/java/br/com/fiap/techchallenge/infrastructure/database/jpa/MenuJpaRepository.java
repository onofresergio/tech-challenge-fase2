package br.com.fiap.techchallenge.infrastructure.database.jpa;

import br.com.fiap.techchallenge.infrastructure.database.entity.MenuEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface MenuJpaRepository extends ListCrudRepository<MenuEntity, Long> {
    List<MenuEntity> findByRestauranteId(Long restauranteId);
}
