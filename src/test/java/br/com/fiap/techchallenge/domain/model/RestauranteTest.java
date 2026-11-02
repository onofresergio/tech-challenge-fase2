package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.exception.RestauranteValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RestauranteTest {
    private final LocalDateTime horarioPadrao = LocalDateTime.of(2026, 10, 12, 18, 0);

    @Test
    @DisplayName("Deve criar um restaurante válido com todos os parâmetros")
    void deveCriarRestauranteValido() {
        // Act
        Restaurante restaurante = new Restaurante(1L, "Restaurante Fiap", 2L, 3L, horarioPadrao, 4L);

        // Assert
        assertThat(restaurante.id()).isEqualTo(1L);
        assertThat(restaurante.nome()).isEqualTo("Restaurante Fiap");
        assertThat(restaurante.enderecoId()).isEqualTo(2L);
        assertThat(restaurante.tipoCozinhaId()).isEqualTo(3L);
        assertThat(restaurante.horarioFuncionamento()).isEqualTo(horarioPadrao);
        assertThat(restaurante.usuarioId()).isEqualTo(4L);
    }

    @Test
    @DisplayName("Deve aplicar trim no nome do restaurante ao instanciar")
    void deveAplicarTrimNoNome() {
        // Act
        Restaurante restaurante = new Restaurante(1L, "   Burger Joint   ", 2L, 3L, horarioPadrao, 4L);

        // Assert
        assertThat(restaurante.nome()).isEqualTo("Burger Joint");
    }

    @Test
    @DisplayName("Deve atualizar o nome corretamente gerando um novo registro (Imutabilidade)")
    void deveAtualizarNomeComSucesso() {
        // Arrange
        Restaurante restauranteOriginal = new Restaurante(1L, "Nome Antigo", 2L, 3L, horarioPadrao, 4L);

        // Act
        Restaurante restauranteAtualizado = restauranteOriginal.updateNome("Nome Novo");

        // Assert
        assertThat(restauranteAtualizado.nome()).isEqualTo("Nome Novo");
        assertThat(restauranteAtualizado.id()).isEqualTo(restauranteOriginal.id());
        assertThat(restauranteAtualizado.enderecoId()).isEqualTo(restauranteOriginal.enderecoId());
        assertThat(restauranteAtualizado.tipoCozinhaId()).isEqualTo(restauranteOriginal.tipoCozinhaId());
        assertThat(restauranteAtualizado.horarioFuncionamento()).isEqualTo(restauranteOriginal.horarioFuncionamento());
        assertThat(restauranteAtualizado.usuarioId()).isEqualTo(restauranteOriginal.usuarioId());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Deve lançar exceção quando o nome for inválido")
    void deveLancarExcecaoQuandoNomeInvalido(String nomeInvalido) {
        assertThatThrownBy(() -> new Restaurante(1L, nomeInvalido, 2L, 3L, horarioPadrao, 4L))
                .isInstanceOf(RestauranteValidationException.class)
                .hasMessage("O nome do restaurante não pode ser vazio.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID do endereço for nulo")
    void deveLancarExcecaoQuandoEnderecoIdForNulo() {
        assertThatThrownBy(() -> new Restaurante(1L, "Restaurante", null, 3L, horarioPadrao, 4L))
                .isInstanceOf(RestauranteValidationException.class)
                .hasMessage("ID de endereço inválido.");
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -99L})
    @DisplayName("Deve lançar exceção quando o ID do endereço for menor ou igual a zero")
    void deveLancarExcecaoQuandoEnderecoIdInvalido(Long idInvalido) {
        assertThatThrownBy(() -> new Restaurante(1L, "Restaurante", idInvalido, 3L, horarioPadrao, 4L))
                .isInstanceOf(RestauranteValidationException.class)
                .hasMessage("ID de endereço inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID do tipo de cozinha for nulo")
    void deveLancarExcecaoQuandoTipoCozinhaIdForNulo() {
        assertThatThrownBy(() -> new Restaurante(1L, "Restaurante", 2L, null, horarioPadrao, 4L))
                .isInstanceOf(RestauranteValidationException.class)
                .hasMessage("ID de tipo de cozinha inválido.");
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -99L})
    @DisplayName("Deve lançar exceção quando o ID do tipo de cozinha for menor ou igual a zero")
    void deveLancarExcecaoQuandoTipoCozinhaIdInvalido(Long idInvalido) {
        assertThatThrownBy(() -> new Restaurante(1L, "Restaurante", 2L, idInvalido, horarioPadrao, 4L))
                .isInstanceOf(RestauranteValidationException.class)
                .hasMessage("ID de tipo de cozinha inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o horário de funcionamento for nulo")
    void deveLancarExcecaoQuandoHorarioFuncionamentoForNulo() {
        assertThatThrownBy(() -> new Restaurante(1L, "Restaurante", 2L, 3L, null, 4L))
                .isInstanceOf(RestauranteValidationException.class)
                .hasMessage("O horário de funcionamento deve ser informado.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID do usuário for nulo")
    void deveLancarExcecaoQuandoUsuarioIdForNulo() {
        assertThatThrownBy(() -> new Restaurante(1L, "Restaurante", 2L, 3L, horarioPadrao, null))
                .isInstanceOf(RestauranteValidationException.class)
                .hasMessage("ID de usuário inválido.");
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -99L})
    @DisplayName("Deve lançar exceção quando o ID do usuário for menor ou igual a zero")
    void deveLancarExcecaoQuandoUsuarioIdInvalido(Long idInvalido) {
        assertThatThrownBy(() -> new Restaurante(1L, "Restaurante", 2L, 3L, horarioPadrao, idInvalido))
                .isInstanceOf(RestauranteValidationException.class)
                .hasMessage("ID de usuário inválido.");
    }
}
