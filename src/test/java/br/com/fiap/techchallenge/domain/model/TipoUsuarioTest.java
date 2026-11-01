package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.exception.TipoUsuarioValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TipoUsuarioTest {

    @Test
    @DisplayName("Deve criar TipoUsuario com sucesso quando os dados forem válidos")
    void deveCriarTipoUsuarioComDadosValidos() {
        TipoUsuario tipoUsuario = new TipoUsuario(1L, "Administrador");

        assertThat(tipoUsuario.id()).isEqualTo(1L);
        assertThat(tipoUsuario.nome()).isEqualTo("Administrador");
    }

    @Test
    @DisplayName("Deve criar TipoUsuario com ID nulo com sucesso")
    void deveCriarTipoUsuarioComIdNulo() {
        TipoUsuario tipoUsuario = new TipoUsuario(null, "Cliente");

        assertThat(tipoUsuario.id()).isNull();
        assertThat(tipoUsuario.nome()).isEqualTo("Cliente");
    }

    @Test
    @DisplayName("Deve remover espaços em branco nas extremidades do nome")
    void deveFazerTrimNoNomeDoTipoUsuario() {
        TipoUsuario tipoUsuario = new TipoUsuario(1L, "   Gerente   ");

        assertThat(tipoUsuario.nome()).isEqualTo("Gerente");
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -100L})
    @DisplayName("Deve lançar exceção quando o ID for menor ou igual a zero")
    void deveLancarExcecaoQuandoIdForInvalido(Long idInvalido) {
        assertThatThrownBy(() -> new TipoUsuario(idInvalido, "Analista"))
                .isInstanceOf(TipoUsuarioValidationException.class)
                .hasMessage("O ID do tipo de usuário deve ser maior que zero.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome for nulo")
    void deveLancarExcecaoQuandoNomeForNulo() {
        assertThatThrownBy(() -> new TipoUsuario(1L, null))
                .isInstanceOf(TipoUsuarioValidationException.class)
                .hasMessage("O nome do tipo de usuário não pode ser vazio.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("Deve lançar exceção quando o nome for vazio ou em branco")
    void deveLancarExcecaoQuandoNomeForVazioOublank(String nomeInvalido) {
        assertThatThrownBy(() -> new TipoUsuario(1L, nomeInvalido))
                .isInstanceOf(TipoUsuarioValidationException.class)
                .hasMessage("O nome do tipo de usuário não pode ser vazio.");
    }
}
