package br.com.fiap.techchallenge.usecase.assembler;

import br.com.fiap.techchallenge.domain.model.Restaurante;
import br.com.fiap.techchallenge.infrastructure.controller.response.EnderecoResponse;
import br.com.fiap.techchallenge.infrastructure.controller.response.RestauranteResponse;
import br.com.fiap.techchallenge.infrastructure.controller.response.TipoCozinhaResponse;
import br.com.fiap.techchallenge.infrastructure.controller.response.TipoUsuarioResponse;
import br.com.fiap.techchallenge.usecase.port.output.EnderecoRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.TipoCozinhaRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class RestauranteResponseAssembler {

    private final EnderecoRepositoryPort enderecoRepositoryPort;
    private final TipoCozinhaRepositoryPort tipoCozinhaRepositoryPort;
    private final TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort;

    public RestauranteResponseAssembler(
            EnderecoRepositoryPort enderecoRepositoryPort,
            TipoCozinhaRepositoryPort tipoCozinhaRepositoryPort,
            TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort) {
        this.enderecoRepositoryPort = enderecoRepositoryPort;
        this.tipoCozinhaRepositoryPort = tipoCozinhaRepositoryPort;
        this.tipoUsuarioRepositoryPort = tipoUsuarioRepositoryPort;
    }

    public RestauranteResponse toResponse(Restaurante restaurante) {
        var endereco = enderecoRepositoryPort.findById(restaurante.enderecoId())
                .map(enderecoEntity -> new EnderecoResponse(
                        enderecoEntity.id(),
                        enderecoEntity.rua(),
                        enderecoEntity.numero(),
                        enderecoEntity.cidade(),
                        enderecoEntity.estado(),
                        enderecoEntity.cep()))
                .orElse(null);

        var tipoCozinha = tipoCozinhaRepositoryPort.findById(restaurante.tipoCozinhaId())
                .map(tipo -> new TipoCozinhaResponse(tipo.id(), tipo.nome()))
                .orElse(null);

        var tipoUsuario = tipoUsuarioRepositoryPort.findById(restaurante.tipoUsuarioId())
                .map(tipo -> new TipoUsuarioResponse(tipo.id(), tipo.nome()))
                .orElse(null);

        return new RestauranteResponse(
                restaurante.id(),
                restaurante.nome(),
                restaurante.horarioFuncionamento(),
                endereco,
                tipoCozinha,
                tipoUsuario);
    }
}
