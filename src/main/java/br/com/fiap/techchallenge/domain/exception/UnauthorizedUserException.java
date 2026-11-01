package br.com.fiap.techchallenge.domain.exception;

public class UnauthorizedUserException extends BusinessException {
    public UnauthorizedUserException(Long tipoUsuarioId) {
        super("O usuário do tipo " + tipoUsuarioId + " não tem permissão para cadastrar restaurantes.");

    }
}
