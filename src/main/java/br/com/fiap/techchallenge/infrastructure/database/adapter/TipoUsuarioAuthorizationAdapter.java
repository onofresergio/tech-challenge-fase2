package br.com.fiap.techchallenge.infrastructure.database.adapter;

import br.com.fiap.techchallenge.domain.model.Usuario;
import br.com.fiap.techchallenge.infrastructure.database.jpa.TipoUsuarioJpaRepository;
import br.com.fiap.techchallenge.infrastructure.database.jpa.UsuarioJpaRepository;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioAuthorizationPort;
import org.springframework.stereotype.Component;

@Component
public class TipoUsuarioAuthorizationAdapter implements TipoUsuarioAuthorizationPort {

    private final UsuarioJpaRepository usuarioRepository;
    private final TipoUsuarioJpaRepository tipoUsuarioRepository;

    public TipoUsuarioAuthorizationAdapter(UsuarioJpaRepository usuarioRepository, TipoUsuarioJpaRepository tipoUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @Override
    public boolean isRestaurantOwner(Long usuarioId) {
        if (usuarioId == null) {
            return false;
        }

        return usuarioRepository.findById(usuarioId)
                .map(entity -> entity.toDomain())
                .map(Usuario::tipoUsuarioId)
                .flatMap(tipoUsuarioRepository::findById)
                .map(tipo -> "Dono de Restaurante".equalsIgnoreCase(tipo.getNome()))
                .orElse(false);
    }
}
