package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.UnauthorizedUserException;
import br.com.fiap.techchallenge.domain.model.Restaurante;
import br.com.fiap.techchallenge.usecase.port.input.CriarRestauranteUseCase;
import br.com.fiap.techchallenge.usecase.port.output.RestauranteRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioAuthorizationPort;

import java.time.LocalDateTime;

public class CriarRestauranteInteractor implements CriarRestauranteUseCase {

    private final RestauranteRepositoryPort restauranteRepositoryPort;
    private final TipoUsuarioAuthorizationPort tipoUsuarioAuthorizationPort;

    public CriarRestauranteInteractor(
            RestauranteRepositoryPort restauranteRepositoryPort,
            TipoUsuarioAuthorizationPort tipoUsuarioAuthorizationPort) {
        this.restauranteRepositoryPort = restauranteRepositoryPort;
        this.tipoUsuarioAuthorizationPort = tipoUsuarioAuthorizationPort;
    }

    @Override
    public Restaurante execute(String nome, Long enderecoId, Long tipoCozinhaId, LocalDateTime horarioFuncionamento, Long usuarioId) {

        if (!tipoUsuarioAuthorizationPort.isRestaurantOwner(usuarioId)) {
            throw new UnauthorizedUserException(usuarioId);
        }

        var restaurante = new Restaurante(null, nome, enderecoId, tipoCozinhaId, horarioFuncionamento, usuarioId);
        return restauranteRepositoryPort.save(restaurante);
    }

}
