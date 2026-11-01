package br.com.fiap.techchallenge.infrastructure.database.adapter;

import br.com.fiap.techchallenge.infrastructure.database.jpa.TipoUsuarioJpaRepository;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioAuthorizationPort;
import org.springframework.stereotype.Component;

@Component
public class TipoUsuarioAuthorizationAdapter implements TipoUsuarioAuthorizationPort {

    private final TipoUsuarioJpaRepository repository;

    public TipoUsuarioAuthorizationAdapter(TipoUsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isRestaurantOwner(Long tipoUsuarioId) {
        return repository.findById(tipoUsuarioId)
                .map(tipo -> "DONO DE RESTAURANTE".equalsIgnoreCase(tipo.getNome()))
                .orElse(false);
    }
}
