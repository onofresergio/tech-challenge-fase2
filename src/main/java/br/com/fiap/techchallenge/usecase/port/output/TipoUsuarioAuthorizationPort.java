package br.com.fiap.techchallenge.usecase.port.output;

public interface TipoUsuarioAuthorizationPort {
    boolean isRestaurantOwner(Long usuarioId);
}
