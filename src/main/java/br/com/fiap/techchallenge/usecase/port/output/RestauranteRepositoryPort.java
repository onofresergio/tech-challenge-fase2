package br.com.fiap.techchallenge.usecase.port.output;

import br.com.fiap.techchallenge.domain.model.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepositoryPort {
    Restaurante save(Restaurante restaurante);
    Optional<Restaurante> findById(Long id);
    List<Restaurante> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
