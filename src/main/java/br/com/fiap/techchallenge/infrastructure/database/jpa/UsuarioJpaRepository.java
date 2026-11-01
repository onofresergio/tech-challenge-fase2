package br.com.fiap.techchallenge.infrastructure.database.jpa;

import br.com.fiap.techchallenge.infrastructure.database.entity.UsuarioEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface UsuarioJpaRepository extends ListCrudRepository<UsuarioEntity, Long> {
    java.util.Optional<UsuarioEntity> findByEmail(String email);
}
