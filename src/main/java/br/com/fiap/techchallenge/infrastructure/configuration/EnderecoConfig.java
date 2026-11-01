package br.com.fiap.techchallenge.infrastructure.configuration;

import br.com.fiap.techchallenge.usecase.interactor.EnderecoInteractor;
import br.com.fiap.techchallenge.usecase.port.input.EnderecoUseCase;
import br.com.fiap.techchallenge.usecase.port.output.EnderecoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnderecoConfig {

    @Bean
    public EnderecoUseCase enderecoUseCase(EnderecoRepositoryPort enderecoRepositoryPort) {
        return new EnderecoInteractor(enderecoRepositoryPort);
    }
}
