package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.RestauranteNotFoundException;
import br.com.fiap.techchallenge.domain.exception.UnauthorizedUserException;
import br.com.fiap.techchallenge.domain.model.Restaurante;
import br.com.fiap.techchallenge.usecase.port.input.UpdateRestauranteUseCase;
import br.com.fiap.techchallenge.usecase.port.output.RestauranteRepositoryPort;

import java.time.LocalDateTime;
import java.util.Objects;

public class AtualizarRestauranteInteractor implements UpdateRestauranteUseCase {

    private final RestauranteRepositoryPort restauranteRepositoryPort;

    public AtualizarRestauranteInteractor(RestauranteRepositoryPort restauranteRepositoryPort) {
        this.restauranteRepositoryPort = restauranteRepositoryPort;
    }

    @Override
    public Restaurante execute(Long id, String nome, Long enderecoId, Long tipoCozinhaId, LocalDateTime horarioFuncionamento, Long usuarioId) {
        var restaurante = restauranteRepositoryPort.findById(id)
                .orElseThrow(() -> new RestauranteNotFoundException(id));

        if (!Objects.equals(restaurante.usuarioId(), usuarioId)) {
            throw new UnauthorizedUserException(usuarioId);
        }

        var restauranteAtualizado = new Restaurante(
                restaurante.id(),
                nome,
                enderecoId,
                tipoCozinhaId,
                horarioFuncionamento,
                usuarioId
        );

        return restauranteRepositoryPort.save(restauranteAtualizado);
    }
}
