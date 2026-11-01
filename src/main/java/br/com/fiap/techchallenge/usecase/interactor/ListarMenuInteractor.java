package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.MenuNotFoundException;
import br.com.fiap.techchallenge.domain.model.Menu;
import br.com.fiap.techchallenge.usecase.port.input.ListaMenuUseCase;
import br.com.fiap.techchallenge.usecase.port.input.MenuQueryUseCase;
import br.com.fiap.techchallenge.usecase.port.output.MenuRepositoryPort;

import java.util.List;

public class ListarMenuInteractor implements ListaMenuUseCase, MenuQueryUseCase {

    private final MenuRepositoryPort menuRepositoryPort;

    public ListarMenuInteractor(MenuRepositoryPort menuRepositoryPort) {
        this.menuRepositoryPort = menuRepositoryPort;
    }

    @Override
    public List<Menu> listar() {
        return menuRepositoryPort.findAll();
    }

    @Override
    public Menu buscarPorId(Long id) {
        return menuRepositoryPort.findById(id)
                .orElseThrow(() -> new MenuNotFoundException(id));
    }
}
