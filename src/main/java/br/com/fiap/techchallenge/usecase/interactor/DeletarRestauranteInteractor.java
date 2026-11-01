package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.RestauranteNotFoundException;
import br.com.fiap.techchallenge.usecase.port.input.DeleteRestauranteUseCase;
import br.com.fiap.techchallenge.usecase.port.output.RestauranteRepositoryPort;

public class DeletarRestauranteInteractor implements DeleteRestauranteUseCase {

    private final RestauranteRepositoryPort restauranteRepositoryPort;

    public DeletarRestauranteInteractor(RestauranteRepositoryPort restauranteRepositoryPort) {
        this.restauranteRepositoryPort = restauranteRepositoryPort;
    }

    @Override
    public void execute(Long id) {
        if (!restauranteRepositoryPort.existsById(id)) {
            throw new RestauranteNotFoundException(id);
        }

        restauranteRepositoryPort.deleteById(id);
    }
}
