package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.model.TipoUsuario;
import br.com.fiap.techchallenge.usecase.port.input.CriarTipoUsuarioUseCase;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioRepositoryPort;

public class CriarTipoUsuarioInteractor implements CriarTipoUsuarioUseCase {

    private final TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort;

    public CriarTipoUsuarioInteractor(TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort) {
        this.tipoUsuarioRepositoryPort = tipoUsuarioRepositoryPort;
    }

    @Override
    public TipoUsuario create(String nome) {
        var tipoUsuario = new TipoUsuario(null, nome);
        return tipoUsuarioRepositoryPort.save(tipoUsuario);
    }
}
