package br.com.fiap.techchallenge.infrastructure.configuration;

import br.com.fiap.techchallenge.usecase.interactor.CriarTipoUsuarioInteractor;
import br.com.fiap.techchallenge.usecase.interactor.TipoUsuarioInteractor;
import br.com.fiap.techchallenge.usecase.port.input.CriarTipoUsuarioUseCase;
import br.com.fiap.techchallenge.usecase.port.input.TipoUsuarioUseCase;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TipoUsuarioConfig {

    @Bean
    public CriarTipoUsuarioUseCase criarTipoUsuarioUseCase(TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort) {
        return new CriarTipoUsuarioInteractor(tipoUsuarioRepositoryPort);
    }

    @Bean
    public TipoUsuarioUseCase tipoUsuarioUseCase(TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort) {
        return new TipoUsuarioInteractor(tipoUsuarioRepositoryPort);
    }
}
