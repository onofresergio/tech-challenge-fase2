package br.com.fiap.techchallenge.domain.exception;

public class UsuarioNotFoundException extends BusinessException {
    public UsuarioNotFoundException(Long id) {
        super("Usuário com ID " + id + " não foi encontrado.");
    }
}
