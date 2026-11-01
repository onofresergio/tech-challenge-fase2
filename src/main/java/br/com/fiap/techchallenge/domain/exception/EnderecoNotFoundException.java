package br.com.fiap.techchallenge.domain.exception;

public class EnderecoNotFoundException extends BusinessException {
    public EnderecoNotFoundException(Long id) {
        super("Endereço com ID " + id + " não foi encontrado.");
    }
}
