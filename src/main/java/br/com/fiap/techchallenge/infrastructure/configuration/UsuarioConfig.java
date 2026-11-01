package br.com.fiap.techchallenge.infrastructure.configuration;

import br.com.fiap.techchallenge.usecase.interactor.UsuarioInteractor;
import br.com.fiap.techchallenge.usecase.port.input.UsuarioUseCase;
import br.com.fiap.techchallenge.usecase.port.output.PasswordHasherPort;
import br.com.fiap.techchallenge.usecase.port.output.UsuarioRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsuarioConfig {

    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort, PasswordHasherPort passwordHasherPort) {
        return new UsuarioInteractor(usuarioRepositoryPort, passwordHasherPort);
    }
}
