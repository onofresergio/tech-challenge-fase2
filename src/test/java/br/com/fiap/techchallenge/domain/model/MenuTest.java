package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.exception.MenuValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {
    @Test
    @DisplayName("Deve criar um menu válido com ID preenchido")
    void deveCriarMenuValidoComId() {
        // Arrange & Act
        Menu menu = new Menu(1L, "Feijoada", "Feijoada completa premium", new BigDecimal("45.90"), true, "/fotos/feijoada.jpg", 10L);

        // Assert
        assertThat(menu.id()).isEqualTo(1L);
        assertThat(menu.nome()).isEqualTo("Feijoada");
        assertThat(menu.descricao()).isEqualTo("Feijoada completa premium");
        assertThat(menu.valorPrato()).isEqualTo(new BigDecimal("45.90"));
        assertThat(menu.disponivelRestaurante()).isTrue();
        assertThat(menu.fotoPrato()).isEqualTo("/fotos/feijoada.jpg");
        assertThat(menu.restauranteId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("Deve criar um menu válido quando o ID for nulo (cenário de novo cadastro)")
    void deveCriarMenuValidoSemId() {
        // Arrange & Act
        Menu menu = new Menu(null, "Feijoada", "Feijoada completa premium", new BigDecimal("45.90"), true, "/fotos/feijoada.jpg", 10L);

        // Assert
        assertThat(menu.id()).isNull();
        assertThat(menu.nome()).isEqualTo("Feijoada");
    }

    @Test
    @DisplayName("Deve remover espaços em branco (trim) dos campos de texto ao instanciar")
    void deveAplicarTrimNosCamposTexto() {
        // Arrange & Act
        Menu menu = new Menu(1L, "  Feijoada  ", "  Feijoada completa premium  ", new BigDecimal("45.90"), true, "  /fotos/feijoada.jpg  ", 10L);

        // Assert
        assertThat(menu.nome()).isEqualTo("Feijoada");
        assertThat(menu.descricao()).isEqualTo("Feijoada completa premium");
        assertThat(menu.fotoPrato()).isEqualTo("/fotos/feijoada.jpg");
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -99L})
    @DisplayName("Deve lançar exceção quando o ID do menu for menor ou igual a zero")
    void deveLancarexcecaoQuandoIdMenuInvalido(Long idInvalido) {
        assertThatThrownBy(() -> new Menu(idInvalido, "Nome", "Descricao", new BigDecimal("10.00"), true, "foto.jpg", 1L))
                .isInstanceOf(MenuValidationException.class)
                .hasMessage("O ID do menu deve ser maior que zero.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Deve lançar exceção quando o nome do prato for inválido")
    void deveLancarexcecaoQuandoNomeInvalido(String nomeInvalido) {
        assertThatThrownBy(() -> new Menu(1L, nomeInvalido, "Descricao", new BigDecimal("10.00"), true, "foto.jpg", 1L))
                .isInstanceOf(MenuValidationException.class)
                .hasMessage("O nome do prato é obrigatório.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Deve lançar exceção quando a descrição do prato for inválida")
    void deveLancarexcecaoQuandoDescricaoInvalida(String descricaoInvalida) {
        assertThatThrownBy(() -> new Menu(1L, "Nome", descricaoInvalida, new BigDecimal("10.00"), true, "foto.jpg", 1L))
                .isInstanceOf(MenuValidationException.class)
                .hasMessage("A descrição do prato é obrigatória.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o valor do prato for nulo")
    void deveLancarexcecaoQuandoValorPratoForNulo() {
        assertThatThrownBy(() -> new Menu(1L, "Nome", "Descricao", null, true, "foto.jpg", 1L))
                .isInstanceOf(MenuValidationException.class)
                .hasMessage("O valor do prato deve ser maior que zero.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.00", "-0.01", "-50.00"})
    @DisplayName("Deve lançar exceção quando o valor do prato for menor ou igual a zero")
    void deveLancarexcecaoQuandoValorPratoForMenorOuIgualAZero(String valorInvalido) {
        BigDecimal valor = new BigDecimal(valorInvalido);
        assertThatThrownBy(() -> new Menu(1L, "Nome", "Descricao", valor, true, "foto.jpg", 1L))
                .isInstanceOf(MenuValidationException.class)
                .hasMessage("O valor do prato deve ser maior que zero.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Deve lançar exceção quando o caminho da foto for inválido")
    void deveLancarexcecaoQuandoFotoPratoInvalida(String fotoInvalida) {
        assertThatThrownBy(() -> new Menu(1L, "Nome", "Descricao", new BigDecimal("10.00"), true, fotoInvalida, 1L))
                .isInstanceOf(MenuValidationException.class)
                .hasMessage("O caminho da foto do prato é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID do restaurante for nulo")
    void deveLancarexcecaoQuandoRestauranteIdForNulo() {
        assertThatThrownBy(() -> new Menu(1L, "Nome", "Descricao", new BigDecimal("10.00"), true, "foto.jpg", null))
                .isInstanceOf(MenuValidationException.class)
                .hasMessage("ID do restaurante inválido.");
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -5L})
    @DisplayName("Deve lançar exceção quando o ID do restaurante for menor ou igual a zero")
    void deveLancarexcecaoQuandoRestauranteIdInvalido(Long restauranteIdInvalido) {
        assertThatThrownBy(() -> new Menu(1L, "Nome", "Descricao", new BigDecimal("10.00"), true, "foto.jpg", restauranteIdInvalido))
                .isInstanceOf(MenuValidationException.class)
                .hasMessage("ID do restaurante inválido.");
    }
}
