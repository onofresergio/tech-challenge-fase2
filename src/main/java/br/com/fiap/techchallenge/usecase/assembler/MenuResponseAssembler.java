package br.com.fiap.techchallenge.usecase.assembler;

import br.com.fiap.techchallenge.domain.model.Menu;
import br.com.fiap.techchallenge.infrastructure.controller.response.EnderecoResponse;
import br.com.fiap.techchallenge.infrastructure.controller.response.MenuResponse;
import br.com.fiap.techchallenge.infrastructure.controller.response.RestauranteResponse;
import br.com.fiap.techchallenge.infrastructure.controller.response.TipoCozinhaResponse;
import br.com.fiap.techchallenge.infrastructure.controller.response.TipoUsuarioResponse;
import br.com.fiap.techchallenge.usecase.port.output.EnderecoRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.RestauranteRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.TipoCozinhaRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class MenuResponseAssembler {

    private final RestauranteRepositoryPort restauranteRepositoryPort;
    private final EnderecoRepositoryPort enderecoRepositoryPort;
    private final TipoCozinhaRepositoryPort tipoCozinhaRepositoryPort;
    private final TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort;

    public MenuResponseAssembler(
            RestauranteRepositoryPort restauranteRepositoryPort,
            EnderecoRepositoryPort enderecoRepositoryPort,
            TipoCozinhaRepositoryPort tipoCozinhaRepositoryPort,
            TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort) {
        this.restauranteRepositoryPort = restauranteRepositoryPort;
        this.enderecoRepositoryPort = enderecoRepositoryPort;
        this.tipoCozinhaRepositoryPort = tipoCozinhaRepositoryPort;
        this.tipoUsuarioRepositoryPort = tipoUsuarioRepositoryPort;
    }

    public MenuResponse toResponse(Menu menu) {
        var restaurante = restauranteRepositoryPort.findById(menu.restauranteId())
                .map(restauranteEntity -> {
                    var endereco = enderecoRepositoryPort.findById(restauranteEntity.enderecoId())
                            .map(enderecoEntity -> new EnderecoResponse(
                                    enderecoEntity.id(),
                                    enderecoEntity.rua(),
                                    enderecoEntity.numero(),
                                    enderecoEntity.cidade(),
                                    enderecoEntity.estado(),
                                    enderecoEntity.cep()))
                            .orElse(null);

                    var tipoCozinha = tipoCozinhaRepositoryPort.findById(restauranteEntity.tipoCozinhaId())
                            .map(tipo -> new TipoCozinhaResponse(tipo.id(), tipo.nome()))
                            .orElse(null);

                    var tipoUsuario = tipoUsuarioRepositoryPort.findById(restauranteEntity.tipoUsuarioId())
                            .map(tipo -> new TipoUsuarioResponse(tipo.id(), tipo.nome()))
                            .orElse(null);

                    return new RestauranteResponse(
                            restauranteEntity.id(),
                            restauranteEntity.nome(),
                            restauranteEntity.horarioFuncionamento(),
                            endereco,
                            tipoCozinha,
                            tipoUsuario);
                })
                .orElse(null);

        return new MenuResponse(
                menu.id(),
                menu.nome(),
                menu.descricao(),
                menu.valorPrato(),
                menu.disponivelRestaurante(),
                menu.fotoPrato(),
                restaurante);
    }
}
