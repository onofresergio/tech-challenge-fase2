package br.com.fiap.techchallenge.usecase.port.output;

import br.com.fiap.techchallenge.domain.model.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuRepositoryPort {
    Menu save(Menu menu);
    Optional<Menu> findById(Long id);
    List<Menu> findAll();
    List<Menu> findAllByRestauranteId(Long restauranteId);
    void deleteById(Long id);
    boolean existsById(Long id);
}
