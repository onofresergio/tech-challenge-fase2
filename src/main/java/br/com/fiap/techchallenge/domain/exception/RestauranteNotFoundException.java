package br.com.fiap.techchallenge.domain.exception;

public class RestauranteNotFoundException extends BusinessException {
    public RestauranteNotFoundException(Long id) {

        super("Restaurante com ID " + id + " não foi localizado.");
    }
}
