package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.exception.UsuarioValidationException;

import java.time.LocalDateTime;

public record Usuario(
        Long id,
        String nome,
        String email,
        String senha,
        String login,
        LocalDateTime dataUltimaAlteracao,
        Long enderecoId,
        Long tipoUsuarioId
) {
    public Usuario {
        if (id != null && id <= 0) {
            throw new UsuarioValidationException("O ID do usuário deve ser maior que zero.");
        }
        nome = nome == null ? null : nome.trim();
        email = email == null ? null : email.trim();
        login = login == null ? null : login.trim();
        if (nome == null || nome.isBlank()) {
            throw new UsuarioValidationException("O nome do usuário é obrigatório.");
        }
        if (email == null || email.isBlank()) {
            throw new UsuarioValidationException("O e-mail do usuário é obrigatório.");
        }
        if (!email.contains("@")) {
            throw new UsuarioValidationException("O e-mail informado é inválido.");
        }
        if (senha == null || senha.isBlank()) {
            throw new UsuarioValidationException("A senha do usuário é obrigatória.");
        }
        if (login == null || login.isBlank()) {
            throw new UsuarioValidationException("O login do usuário é obrigatório.");
        }
        if (enderecoId == null || enderecoId <= 0) {
            throw new UsuarioValidationException("ID de endereço inválido.");
        }
        if (tipoUsuarioId == null || tipoUsuarioId <= 0) {
            throw new UsuarioValidationException("ID de tipo de usuário inválido.");
        }
    }
}
