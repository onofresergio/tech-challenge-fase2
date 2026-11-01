package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.EnderecoNotFoundException;
import br.com.fiap.techchallenge.domain.model.Endereco;
import br.com.fiap.techchallenge.usecase.port.input.EnderecoUseCase;
import br.com.fiap.techchallenge.usecase.port.output.EnderecoRepositoryPort;

import java.util.List;

public class EnderecoInteractor implements EnderecoUseCase {

    private final EnderecoRepositoryPort enderecoRepositoryPort;

    public EnderecoInteractor(EnderecoRepositoryPort enderecoRepositoryPort) {
        this.enderecoRepositoryPort = enderecoRepositoryPort;
    }

    @Override
    public Endereco findById(Long id) {
        return enderecoRepositoryPort.findById(id)
                .orElseThrow(() -> new EnderecoNotFoundException(id));
    }

    @Override
    public Endereco create(String rua, String numero, String cidade, String estado, String cep) {
        var endereco = new Endereco(null, rua, numero, cidade, estado, cep);
        return enderecoRepositoryPort.save(endereco);
    }

    @Override
    public Endereco update(Long id, String rua, String numero, String cidade, String estado, String cep) {
        if (!enderecoRepositoryPort.existsById(id)) {
            throw new EnderecoNotFoundException(id);
        }
        var endereco = new Endereco(id, rua, numero, cidade, estado, cep);
        return enderecoRepositoryPort.save(endereco);
    }

    @Override
    public void delete(Long id) {
        if (!enderecoRepositoryPort.existsById(id)) {
            throw new EnderecoNotFoundException(id);
        }
        enderecoRepositoryPort.deleteById(id);
    }

    @Override
    public List<Endereco> findAll() {
        return enderecoRepositoryPort.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return enderecoRepositoryPort.existsById(id);
    }
}
