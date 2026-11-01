package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.exception.RestauranteValidationException;

import java.time.LocalDateTime;

public record Restaurante(
        Long id,
        String nome,
        Long enderecoId,
        Long tipoCozinhaId,
        LocalDateTime horarioFuncionamento,
        //LocalDateTime dataUltimaAlteracao,
        Long tipoUsuarioId
) {
    // Construtor Compacto para validações invariantes
    public Restaurante {
        nome = nome == null ? null : nome.trim();
        if (nome == null || nome.isBlank()) {
            throw new RestauranteValidationException("O nome do restaurante não pode ser vazio.");
        }
        if (enderecoId == null || enderecoId <= 0) {
            throw new RestauranteValidationException("ID de endereço inválido.");
        }
        if (tipoCozinhaId == null || tipoCozinhaId <= 0) {
            throw new RestauranteValidationException("ID de tipo de cozinha inválido.");
        }
        if (horarioFuncionamento == null) {
            throw new RestauranteValidationException("O horário de funcionamento deve ser informado.");
        }
        if (tipoUsuarioId == null || tipoUsuarioId <= 0) {
            throw new RestauranteValidationException("ID de tipo de usuário inválido.");
        }
    }

    public Restaurante updateNome(String nome) {
        return new Restaurante(
                this.id,
                nome,
                this.enderecoId,
                this.tipoCozinhaId,
                this.horarioFuncionamento,
                //dataUltimaAlteracao,
                this.tipoUsuarioId
        );
    }
}
