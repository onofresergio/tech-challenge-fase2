package br.com.fiap.techchallenge.usecase.port.input;

import br.com.fiap.techchallenge.domain.model.Endereco;

public interface EnderecoCommandUseCase {
    Endereco create(String rua, String numero, String cidade, String estado, String cep);
    Endereco update(Long id, String rua, String numero, String cidade, String estado, String cep);
    void delete(Long id);
}
