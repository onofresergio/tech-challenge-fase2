package br.com.fiap.techchallenge.usecase.assembler;

import br.com.fiap.techchallenge.domain.model.Usuario;
import br.com.fiap.techchallenge.infrastructure.controller.response.EnderecoResponse;
import br.com.fiap.techchallenge.infrastructure.controller.response.TipoUsuarioResponse;
import br.com.fiap.techchallenge.infrastructure.controller.response.UsuarioResponse;
import br.com.fiap.techchallenge.usecase.port.output.EnderecoRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class UsuarioResponseAssembler {

    private final EnderecoRepositoryPort enderecoRepositoryPort;
    private final TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort;

    public UsuarioResponseAssembler(
            EnderecoRepositoryPort enderecoRepositoryPort,
            TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort) {
        this.enderecoRepositoryPort = enderecoRepositoryPort;
        this.tipoUsuarioRepositoryPort = tipoUsuarioRepositoryPort;
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        var endereco = enderecoRepositoryPort.findById(usuario.enderecoId())
                .map(enderecoEntity -> new EnderecoResponse(
                        enderecoEntity.id(),
                        enderecoEntity.rua(),
                        enderecoEntity.numero(),
                        enderecoEntity.cidade(),
                        enderecoEntity.estado(),
                        enderecoEntity.cep()))
                .orElse(null);

        var tipoUsuario = tipoUsuarioRepositoryPort.findById(usuario.tipoUsuarioId())
                .map(tipo -> new TipoUsuarioResponse(tipo.id(), tipo.nome()))
                .orElse(null);

        return new UsuarioResponse(
                usuario.id(),
                usuario.nome(),
                usuario.email(),
                usuario.login(),
                endereco,
                tipoUsuario);
    }
}
