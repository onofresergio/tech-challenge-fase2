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
    public Restaurante execute(Long id, String nome, Long enderecoId, Long tipoCozinhaId, LocalDateTime horarioFuncionamento, Long tipoUsuarioId) {
        var restaurante = restauranteRepositoryPort.findById(id)
                .orElseThrow(() -> new RestauranteNotFoundException(id));

        if (!Objects.equals(restaurante.tipoUsuarioId(), tipoUsuarioId)) {
            throw new UnauthorizedUserException(tipoUsuarioId);
        }

        var restauranteAtualizado = new Restaurante(
                restaurante.id(),
                nome,
                enderecoId, //restaurante.enderecoId(),
                tipoCozinhaId, //restaurante.tipoCozinhaId(),
                horarioFuncionamento,
                restaurante.tipoUsuarioId()
        );

        return restauranteRepositoryPort.save(restauranteAtualizado);
    }
}
