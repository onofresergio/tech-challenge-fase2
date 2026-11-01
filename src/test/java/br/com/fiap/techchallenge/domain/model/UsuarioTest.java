package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.exception.UsuarioValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UsuarioTest {
    private final LocalDateTime dataPadrao = LocalDateTime.of(2026, 10, 12, 13, 0);

    @Test
    @DisplayName("Deve criar um usuário válido com ID preenchido")
    void deveCriarUsuarioValidoComId() {
        // Act
        Usuario usuario = new Usuario(1L, "Onofre", "teste@teste.com.br", "senha123", "teste", dataPadrao, 2L, 3L);

        // Assert
        assertThat(usuario.id()).isEqualTo(1L);
        assertThat(usuario.nome()).isEqualTo("Onofre");
        assertThat(usuario.email()).isEqualTo("teste@teste.com.br");
        assertThat(usuario.senha()).isEqualTo("senha123");
        assertThat(usuario.login()).isEqualTo("teste");
        assertThat(usuario.dataUltimaAlteracao()).isEqualTo(dataPadrao);
        assertThat(usuario.enderecoId()).isEqualTo(2L);
        assertThat(usuario.tipoUsuarioId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("Deve criar um usuário válido quando o ID for nulo (cenário de novo cadastro)")
    void deveCriarUsuarioValidoSemId() {
        // Act
        Usuario usuario = new Usuario(null, "Onofre", "teste@teste.com.br", "senha123", "teste", dataPadrao, 2L, 3L);

        // Assert
        assertThat(usuario.id()).isNull();
        assertThat(usuario.nome()).isEqualTo("Onofre");
    }

    @Test
    @DisplayName("Deve remover espaços em branco (trim) dos campos de texto correspondentes")
    void deveAplicarTrimNosCamposTexto() {
        // Act
        Usuario usuario = new Usuario(1L, "  Onofre  ", "  teste@teste.com.br  ", "senha123", "  teste  ", dataPadrao, 2L, 3L);

        // Assert
        assertThat(usuario.nome()).isEqualTo("Onofre");
        assertThat(usuario.email()).isEqualTo("teste@teste.com.br");
        assertThat(usuario.login()).isEqualTo("teste");
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -99L})
    @DisplayName("Deve lançar exceção quando o ID do usuário for inválido")
    void deveLancarExcecaoQuandoIdInvalido(Long idInvalido) {
        assertThatThrownBy(() -> new Usuario(idInvalido, "Nome", "email@test.com", "senha", "login", dataPadrao, 1L, 1L))
                .isInstanceOf(UsuarioValidationException.class)
                .hasMessage("O ID do usuário deve ser maior que zero.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Deve lançar exceção quando o nome do usuário for inválido")
    void deveLancarExcecaoQuandoNomeInvalido(String nomeInvalido) {
        assertThatThrownBy(() -> new Usuario(1L, nomeInvalido, "email@test.com", "senha", "login", dataPadrao, 1L, 1L))
                .isInstanceOf(UsuarioValidationException.class)
                .hasMessage("O nome do usuário é obrigatório.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Deve lançar exceção quando o e-mail do usuário for vazio ou nulo")
    void deveLancarExcecaoQuandoEmailForVazioOuNulo(String emailInvalido) {
        assertThatThrownBy(() -> new Usuario(1L, "Nome", emailInvalido, "senha", "login", dataPadrao, 1L, 1L))
                .isInstanceOf(UsuarioValidationException.class)
                .hasMessage("O e-mail do usuário é obrigatório.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"://exemplo.com", "teste", "://teste.com"}) // Removido o '@' do último item
    @DisplayName("Deve lançar exceção quando o formato do e-mail não contiver o caractere @")
    void deveLancarExcecaoQuandoEmailNaoContemArroba(String emailSemArroba) {
        assertThatThrownBy(() -> new Usuario(1L, "Nome", emailSemArroba, "senha", "login", dataPadrao, 1L, 1L))
                .isInstanceOf(UsuarioValidationException.class)
                .hasMessage("O e-mail informado é inválido.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Deve lançar exceção quando a senha do usuário for inválida")
    void deveLancarExcecaoQuandoSenhaInvalida(String senhaInvalida) {
        assertThatThrownBy(() -> new Usuario(1L, "Nome", "email@test.com", senhaInvalida, "login", dataPadrao, 1L, 1L))
                .isInstanceOf(UsuarioValidationException.class)
                .hasMessage("A senha do usuário é obrigatória.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Deve lançar exceção quando o login do usuário for inválido")
    void deveLancarExcecaoQuandoLoginInvalido(String loginInvalido) {
        assertThatThrownBy(() -> new Usuario(1L, "Nome", "email@test.com", "senha", loginInvalido, dataPadrao, 1L, 1L))
                .isInstanceOf(UsuarioValidationException.class)
                .hasMessage("O login do usuário é obrigatório.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID do endereço for nulo")
    void deveLancarExcecaoQuandoEnderecoIdForNulo() {
        assertThatThrownBy(() -> new Usuario(1L, "Nome", "email@test.com", "senha", "login", dataPadrao, null, 1L))
                .isInstanceOf(UsuarioValidationException.class)
                .hasMessage("ID de endereço inválido.");
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -5L})
    @DisplayName("Deve lançar exceção quando o ID do endereço for menor ou igual a zero")
    void deveLancarExcecaoQuandoEnderecoIdInvalido(Long enderecoIdInvalido) {
        assertThatThrownBy(() -> new Usuario(1L, "Nome", "email@test.com", "senha", "login", dataPadrao, enderecoIdInvalido, 1L))
                .isInstanceOf(UsuarioValidationException.class)
                .hasMessage("ID de endereço inválido.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID do tipo de usuário for nulo")
    void deveLancarExcecaoQuandoTipoUsuarioIdForNulo() {
        assertThatThrownBy(() -> new Usuario(1L, "Nome", "email@test.com", "senha", "login", dataPadrao, 1L, null))
                .isInstanceOf(UsuarioValidationException.class)
                .hasMessage("ID de tipo de usuário inválido.");
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -5L})
    @DisplayName("Deve lançar exceção quando o ID do tipo de usuário for menor ou igual a zero")
    void deveLancarExcecaoQuandoTipoUsuarioIdInvalido(Long tipoUsuarioIdInvalido) {
        assertThatThrownBy(() -> new Usuario(1L, "Nome", "email@test.com", "senha", "login", dataPadrao, 1L, tipoUsuarioIdInvalido))
                .isInstanceOf(UsuarioValidationException.class)
                .hasMessage("ID de tipo de usuário inválido.");
    }
}
