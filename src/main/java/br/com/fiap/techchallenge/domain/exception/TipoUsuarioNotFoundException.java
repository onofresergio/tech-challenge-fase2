package br.com.fiap.techchallenge.domain.exception;

public class TipoUsuarioNotFoundException extends BusinessException {
    public TipoUsuarioNotFoundException(Long id) {
        super("Tipo de usuário com ID " + id + " não foi encontrado.");
    }
}
