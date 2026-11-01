package br.com.fiap.techchallenge.infrastructure.configuration;

import br.com.fiap.techchallenge.usecase.interactor.AtualizarMenuInteractor;
import br.com.fiap.techchallenge.usecase.interactor.CriarMenuInteractor;
import br.com.fiap.techchallenge.usecase.interactor.DeletarMenuInteractor;
import br.com.fiap.techchallenge.usecase.interactor.ListarMenuInteractor;
import br.com.fiap.techchallenge.usecase.port.input.CriarMenuUseCase;
import br.com.fiap.techchallenge.usecase.port.input.DeleteMenuUseCase;
import br.com.fiap.techchallenge.usecase.port.input.ListaMenuUseCase;
import br.com.fiap.techchallenge.usecase.port.input.MenuQueryUseCase;
import br.com.fiap.techchallenge.usecase.port.input.UpdateMenuUseCase;
import br.com.fiap.techchallenge.usecase.port.output.MenuRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MenuConfig {

    @Bean
    public CriarMenuUseCase criarMenuUseCase(MenuRepositoryPort menuRepositoryPort) {
        return new CriarMenuInteractor(menuRepositoryPort);
    }

    @Bean
    public UpdateMenuUseCase updateMenuUseCase(MenuRepositoryPort menuRepositoryPort) {
        return new AtualizarMenuInteractor(menuRepositoryPort);
    }

    @Bean
    public DeleteMenuUseCase deleteMenuUseCase(MenuRepositoryPort menuRepositoryPort) {
        return new DeletarMenuInteractor(menuRepositoryPort);
    }

    @Bean
    public ListaMenuUseCase listaMenuUseCase(MenuRepositoryPort menuRepositoryPort) {
        return new ListarMenuInteractor(menuRepositoryPort);
    }

    @Bean
    public MenuQueryUseCase menuQueryUseCase(MenuRepositoryPort menuRepositoryPort) {
        return new ListarMenuInteractor(menuRepositoryPort);
    }
}
