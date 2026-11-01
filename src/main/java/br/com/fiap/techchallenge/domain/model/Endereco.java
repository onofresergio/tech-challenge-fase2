package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.exception.EnderecoValidationException;

public record Endereco(
        Long id,
        String rua,
        String numero,
        String cidade,
        String estado,
        String cep
) {
    public Endereco {
        if (id != null && id <= 0) {
            throw new EnderecoValidationException("O ID do endereço deve ser maior que zero.");
        }
        rua = rua == null ? null : rua.trim();
        numero = numero == null ? null : numero.trim();
        cidade = cidade == null ? null : cidade.trim();
        estado = estado == null ? null : estado.trim();
        cep = cep == null ? null : cep.trim();

        if (rua == null || rua.isBlank()) {
            throw new EnderecoValidationException("A rua do endereço é obrigatória.");
        }
        if (numero == null || numero.isBlank()) {
            throw new EnderecoValidationException("O número do endereço é obrigatório.");
        }
        if (cidade == null || cidade.isBlank()) {
            throw new EnderecoValidationException("A cidade do endereço é obrigatória.");
        }
        if (estado == null || estado.isBlank()) {
            throw new EnderecoValidationException("O estado do endereço é obrigatório.");
        }
        if (cep == null || cep.isBlank()) {
            throw new EnderecoValidationException("O CEP do endereço é obrigatório.");
        }
    }
}
