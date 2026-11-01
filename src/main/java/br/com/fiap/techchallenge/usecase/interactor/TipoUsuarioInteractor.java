package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.TipoUsuarioNotFoundException;
import br.com.fiap.techchallenge.domain.model.TipoUsuario;
import br.com.fiap.techchallenge.usecase.port.input.TipoUsuarioUseCase;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioRepositoryPort;

import java.util.List;

public class TipoUsuarioInteractor implements TipoUsuarioUseCase {

    private final TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort;

    public TipoUsuarioInteractor(TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort) {
        this.tipoUsuarioRepositoryPort = tipoUsuarioRepositoryPort;
    }

    @Override
    public TipoUsuario update(Long id, String nome) {
        if (!tipoUsuarioRepositoryPort.existsById(id)) {
            throw new TipoUsuarioNotFoundException(id);
        }
        var tipo = new TipoUsuario(id, nome);
        return tipoUsuarioRepositoryPort.save(tipo);
    }

    @Override
    public TipoUsuario findById(Long id) {
        return tipoUsuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new TipoUsuarioNotFoundException(id));
    }

    @Override
    public List<TipoUsuario> findAll() {
        return tipoUsuarioRepositoryPort.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return tipoUsuarioRepositoryPort.findById(id).isPresent();
    }

    @Override
    public void delete(Long id) {
        if (!tipoUsuarioRepositoryPort.existsById(id)) {
            throw new TipoUsuarioNotFoundException(id);
        }
        tipoUsuarioRepositoryPort.deleteById(id);
    }

}
