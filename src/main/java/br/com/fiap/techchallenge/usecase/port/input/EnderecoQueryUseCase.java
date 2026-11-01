package br.com.fiap.techchallenge.usecase.port.input;

import br.com.fiap.techchallenge.domain.model.Endereco;

import java.util.List;

public interface EnderecoQueryUseCase {
    Endereco findById(Long id);
    List<Endereco> findAll();
    boolean existsById(Long id);
}
