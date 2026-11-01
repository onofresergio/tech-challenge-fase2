package br.com.fiap.techchallenge.infrastructure.database.jpa;

import br.com.fiap.techchallenge.infrastructure.database.entity.EnderecoEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface EnderecoJpaRepository extends ListCrudRepository<EnderecoEntity, Long> {
}
