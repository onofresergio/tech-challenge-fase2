package br.com.fiap.techchallenge.infrastructure.database.jpa;

import br.com.fiap.techchallenge.infrastructure.database.entity.TipoCozinhaEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface TipoCozinhaJpaRepository extends ListCrudRepository<TipoCozinhaEntity, Long> {
}
