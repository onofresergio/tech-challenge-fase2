package br.com.fiap.techchallenge.usecase.port.output;

import br.com.fiap.techchallenge.domain.model.TipoCozinha;

import java.util.List;
import java.util.Optional;

public interface TipoCozinhaRepositoryPort {
    TipoCozinha save(TipoCozinha tipoCozinha);
    List<TipoCozinha> findAll();
    Optional<TipoCozinha> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);
}
