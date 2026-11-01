package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.MenuNotFoundException;
import br.com.fiap.techchallenge.domain.model.Menu;
import br.com.fiap.techchallenge.usecase.port.input.UpdateMenuUseCase;
import br.com.fiap.techchallenge.usecase.port.output.MenuRepositoryPort;

import java.math.BigDecimal;

public class AtualizarMenuInteractor implements UpdateMenuUseCase {

    private final MenuRepositoryPort menuRepositoryPort;

    public AtualizarMenuInteractor(MenuRepositoryPort menuRepositoryPort) {
        this.menuRepositoryPort = menuRepositoryPort;
    }

    @Override
    public Menu execute(Long id, String nome, String descricao, BigDecimal valorPrato, boolean disponivelRestaurante, String fotoPrato) {
        var menuAtual = menuRepositoryPort.findById(id)
                .orElseThrow(() -> new MenuNotFoundException(id));

        var menuAtualizado = new Menu(
                menuAtual.id(),
                nome,
                descricao,
                valorPrato,
                disponivelRestaurante,
                fotoPrato,
                menuAtual.restauranteId()
        );

        return menuRepositoryPort.save(menuAtualizado);
    }
}
