package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.MenuNotFoundException;
import br.com.fiap.techchallenge.usecase.port.input.DeleteMenuUseCase;
import br.com.fiap.techchallenge.usecase.port.output.MenuRepositoryPort;

public class DeletarMenuInteractor implements DeleteMenuUseCase {

    private final MenuRepositoryPort menuRepositoryPort;

    public DeletarMenuInteractor(MenuRepositoryPort menuRepositoryPort) {
        this.menuRepositoryPort = menuRepositoryPort;
    }

    @Override
    public void execute(Long id) {
        if (!menuRepositoryPort.existsById(id)) {
            throw new MenuNotFoundException(id);
        }

        menuRepositoryPort.deleteById(id);
    }
}
