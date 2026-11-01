package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.model.Menu;
import br.com.fiap.techchallenge.usecase.port.input.CriarMenuUseCase;
import br.com.fiap.techchallenge.usecase.port.output.MenuRepositoryPort;

import java.math.BigDecimal;

public class CriarMenuInteractor implements CriarMenuUseCase {

    private final MenuRepositoryPort menuRepositoryPort;

    public CriarMenuInteractor(MenuRepositoryPort menuRepositoryPort) {
        this.menuRepositoryPort = menuRepositoryPort;
    }

    @Override
    public Menu execute(String nome, String descricao, BigDecimal valorPrato, boolean disponivelRestaurante, String fotoPrato, Long restauranteId) {
        var menu = new Menu(null, nome, descricao, valorPrato, disponivelRestaurante, fotoPrato, restauranteId);
        return menuRepositoryPort.save(menu);
    }
}
