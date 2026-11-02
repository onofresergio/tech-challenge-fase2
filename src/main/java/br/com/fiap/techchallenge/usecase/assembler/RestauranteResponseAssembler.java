package br.com.fiap.techchallenge.usecase.assembler;

import br.com.fiap.techchallenge.domain.model.Restaurante;
import br.com.fiap.techchallenge.infrastructure.controller.response.EnderecoResponse;
import br.com.fiap.techchallenge.infrastructure.controller.response.RestauranteResponse;
import br.com.fiap.techchallenge.infrastructure.controller.response.TipoCozinhaResponse;
import br.com.fiap.techchallenge.infrastructure.controller.response.TipoUsuarioResponse;
import br.com.fiap.techchallenge.infrastructure.controller.response.UsuarioResponse;
import br.com.fiap.techchallenge.usecase.port.output.EnderecoRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.TipoCozinhaRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class RestauranteResponseAssembler {

    private final EnderecoRepositoryPort enderecoRepositoryPort;
    private final TipoCozinhaRepositoryPort tipoCozinhaRepositoryPort;
    private final TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public RestauranteResponseAssembler(
            EnderecoRepositoryPort enderecoRepositoryPort,
            TipoCozinhaRepositoryPort tipoCozinhaRepositoryPort,
            TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort,
            UsuarioRepositoryPort usuarioRepositoryPort) {
        this.enderecoRepositoryPort = enderecoRepositoryPort;
        this.tipoCozinhaRepositoryPort = tipoCozinhaRepositoryPort;
        this.tipoUsuarioRepositoryPort = tipoUsuarioRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
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

        var usuarioResponsavel = usuarioRepositoryPort.findById(restaurante.usuarioId())
                .map(usuarioEntity -> {
                    var usuarioEndereco = enderecoRepositoryPort.findById(usuarioEntity.enderecoId())
                            .map(enderecoEntity -> new EnderecoResponse(
                                    enderecoEntity.id(),
                                    enderecoEntity.rua(),
                                    enderecoEntity.numero(),
                                    enderecoEntity.cidade(),
                                    enderecoEntity.estado(),
                                    enderecoEntity.cep()))
                            .orElse(null);

                    var tipoUsuarioDoResponsavel = tipoUsuarioRepositoryPort.findById(usuarioEntity.tipoUsuarioId())
                            .map(tipo -> new TipoUsuarioResponse(tipo.id(), tipo.nome()))
                            .orElse(null);

                    return new UsuarioResponse(
                            usuarioEntity.id(),
                            usuarioEntity.nome(),
                            usuarioEntity.email(),
                            usuarioEntity.login(),
                            usuarioEndereco,
                            tipoUsuarioDoResponsavel);
                })
                .orElse(null);

        return new RestauranteResponse(
                restaurante.id(),
                restaurante.nome(),
                restaurante.horarioFuncionamento(),
                endereco,
                tipoCozinha,
                usuarioResponsavel);
    }
}
