package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.exception.TipoCozinhaValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TipoCozinhaTest {
    @Test
    @DisplayName("Deve criar um tipo de cozinha válido com ID e nome preenchidos")
    void deveCriarTipoCozinhaValido() {
        // Act
        TipoCozinha tipoCozinha = new TipoCozinha(1L, "Italiana");

        // Assert
        assertThat(tipoCozinha.id()).isEqualTo(1L);
        assertThat(tipoCozinha.nome()).isEqualTo("Italiana");
    }

    @Test
    @DisplayName("Deve aplicar trim no nome do tipo de cozinha após a validação")
    void deveAplicarTrimNoNome() {
        // Act
        TipoCozinha tipoCozinha = new TipoCozinha(1L, "   Japonesa   ");

        // Assert
        assertThat(tipoCozinha.nome()).isEqualTo("Japonesa");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID do tipo de cozinha for nulo")
    void deveLancarExcecaoQuandoIdForNulo() {
        assertThatThrownBy(() -> new TipoCozinha(null, "Nordestina"))
                .isInstanceOf(TipoCozinhaValidationException.class)
                .hasMessage("O ID do tipo de cozinha deve ser um número positivo.");
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -99L})
    @DisplayName("Deve lançar exceção quando o ID do tipo de cozinha for menor ou igual a zero")
    void deveLancarExcecaoQuandoIdInvalido(Long idInvalido) {
        assertThatThrownBy(() -> new TipoCozinha(idInvalido, "Nordestina"))
                .isInstanceOf(TipoCozinhaValidationException.class)
                .hasMessage("O ID do tipo de cozinha deve ser um número positivo.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Deve lançar exceção quando o nome do tipo de cozinha for inválido")
    void deveLancarExcecaoQuandoNomeInvalido(String nomeInvalido) {
        assertThatThrownBy(() -> new TipoCozinha(1L, nomeInvalido))
                .isInstanceOf(TipoCozinhaValidationException.class)
                .hasMessage("O nome do tipo de cozinha não pode ser vazio.");
    }
}
