package br.com.fiap.techchallenge.infrastructure.database.jpa;

import br.com.fiap.techchallenge.infrastructure.database.entity.TipoUsuarioEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface TipoUsuarioJpaRepository extends ListCrudRepository<TipoUsuarioEntity, Long> {
}
