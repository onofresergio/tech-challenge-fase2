package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.RestauranteNotFoundException;
import br.com.fiap.techchallenge.domain.model.Restaurante;
import br.com.fiap.techchallenge.usecase.port.input.ListaRestauranteUseCase;
import br.com.fiap.techchallenge.usecase.port.input.RestauranteQueryUseCase;
import br.com.fiap.techchallenge.usecase.port.output.RestauranteRepositoryPort;

import java.util.List;

public class ListarRestauranteInteractor implements ListaRestauranteUseCase, RestauranteQueryUseCase {

    private final RestauranteRepositoryPort restauranteRepositoryPort;

    public ListarRestauranteInteractor(RestauranteRepositoryPort restauranteRepositoryPort) {
        this.restauranteRepositoryPort = restauranteRepositoryPort;
    }

    @Override
    public List<Restaurante> listar() {
        return restauranteRepositoryPort.findAll();
    }

    @Override
    public Restaurante buscarPorId(Long id) {
        return restauranteRepositoryPort.findById(id)
                .orElseThrow(() -> new RestauranteNotFoundException(id));
    }
}
