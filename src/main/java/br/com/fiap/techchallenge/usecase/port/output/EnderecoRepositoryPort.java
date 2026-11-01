package br.com.fiap.techchallenge.usecase.port.output;

import br.com.fiap.techchallenge.domain.model.Endereco;

import java.util.List;
import java.util.Optional;

public interface EnderecoRepositoryPort {
    Endereco save(Endereco endereco);
    List<Endereco> findAll();
    Optional<Endereco> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);
}
