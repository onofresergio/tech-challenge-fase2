package br.com.fiap.techchallenge.infrastructure.configuration;

import br.com.fiap.techchallenge.usecase.interactor.AtualizarRestauranteInteractor;
import br.com.fiap.techchallenge.usecase.interactor.CriarRestauranteInteractor;
import br.com.fiap.techchallenge.usecase.interactor.DeletarRestauranteInteractor;
import br.com.fiap.techchallenge.usecase.interactor.ListarRestauranteInteractor;
import br.com.fiap.techchallenge.usecase.port.input.CriarRestauranteUseCase;
import br.com.fiap.techchallenge.usecase.port.input.DeleteRestauranteUseCase;
import br.com.fiap.techchallenge.usecase.port.input.ListaRestauranteUseCase;
import br.com.fiap.techchallenge.usecase.port.input.RestauranteQueryUseCase;
import br.com.fiap.techchallenge.usecase.port.input.UpdateRestauranteUseCase;
import br.com.fiap.techchallenge.usecase.port.output.RestauranteRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioAuthorizationPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestauranteConfig {
    @Bean
    public CriarRestauranteUseCase criarRestauranteUseCase(
            RestauranteRepositoryPort restauranteRepositoryPort,
            TipoUsuarioAuthorizationPort tipoUsuarioAuthorizationPort) {
        return new CriarRestauranteInteractor(restauranteRepositoryPort, tipoUsuarioAuthorizationPort);
    }

    @Bean
    public UpdateRestauranteUseCase updateRestauranteUseCase(RestauranteRepositoryPort restauranteRepositoryPort) {
        return new AtualizarRestauranteInteractor(restauranteRepositoryPort);
    }

    @Bean
    public DeleteRestauranteUseCase deleteRestauranteUseCase(RestauranteRepositoryPort restauranteRepositoryPort) {
        return new DeletarRestauranteInteractor(restauranteRepositoryPort);
    }

    @Bean
    public ListaRestauranteUseCase listaRestauranteUseCase(RestauranteRepositoryPort restauranteRepositoryPort) {
        return new ListarRestauranteInteractor(restauranteRepositoryPort);
    }

    @Bean
    public RestauranteQueryUseCase restauranteQueryUseCase(RestauranteRepositoryPort restauranteRepositoryPort) {
        return new ListarRestauranteInteractor(restauranteRepositoryPort);
    }
}
