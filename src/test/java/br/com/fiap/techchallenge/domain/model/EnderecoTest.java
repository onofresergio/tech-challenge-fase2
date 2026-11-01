package br.com.fiap.techchallenge.domain.model;

import br.com.fiap.techchallenge.domain.exception.EnderecoValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EnderecoTest {
    @Test
    @DisplayName("Deve criar um endereço válido com ID preenchido")
    void deveCriarEnderecoValidoComId() {
        // Act
        Endereco endereco = new Endereco(1L, "Av. Paulista", "1000", "São Paulo", "SP", "01311-100");

        // Assert
        assertThat(endereco.id()).isEqualTo(1L);
        assertThat(endereco.rua()).isEqualTo("Av. Paulista");
        assertThat(endereco.numero()).isEqualTo("1000");
        assertThat(endereco.cidade()).isEqualTo("São Paulo");
        assertThat(endereco.estado()).isEqualTo("SP");
        assertThat(endereco.cep()).isEqualTo("01311-100");
    }

    @Test
    @DisplayName("Deve criar um endereço válido quando o ID for nulo (cenário de novo cadastro)")
    void deveCriarEnderecoValidoSemId() {
        // Act
        Endereco endereco = new Endereco(null, "Av. Paulista", "1000", "São Paulo", "SP", "01311-100");

        // Assert
        assertThat(endereco.id()).isNull();
        assertThat(endereco.rua()).isEqualTo("Av. Paulista");
    }

    @Test
    @DisplayName("Deve remover espaços em branco (trim) dos campos de texto ao instanciar")
    void deveAplicarTrimNosCamposTexto() {
        // Act
        Endereco endereco = new Endereco(1L, "  Rua Augusta  ", " 150 ", " São Paulo ", " SP ", " 01305-000 ");

        // Assert
        assertThat(endereco.rua()).isEqualTo("Rua Augusta");
        assertThat(endereco.numero()).isEqualTo("150");
        assertThat(endereco.cidade()).isEqualTo("São Paulo");
        assertThat(endereco.estado()).isEqualTo("SP");
        assertThat(endereco.cep()).isEqualTo("01305-000");
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -99L})
    @DisplayName("Deve lançar exceção quando o ID for menor ou igual a zero")
    void deveLancarexcecaoQuandoIdInvalido(Long idInvalido) {
        assertThatThrownBy(() -> new Endereco(idInvalido, "Rua A", "123", "Cidade", "ST", "12345-678"))
                .isInstanceOf(EnderecoValidationException.class)
                .hasMessage("O ID do endereço deve ser maior que zero.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Deve lançar exceção quando a rua for inválida")
    void deveLancarexcecaoQuandoRuaInvalida(String ruaInvalida) {
        assertThatThrownBy(() -> new Endereco(1L, ruaInvalida, "123", "Cidade", "ST", "12345-678"))
                .isInstanceOf(EnderecoValidationException.class)
                .hasMessage("A rua do endereço é obrigatória.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Deve lançar exceção quando o número for inválido")
    void deveLancarexcecaoQuandoNumeroInvalido(String numeroInvalido) {
        assertThatThrownBy(() -> new Endereco(1L, "Rua A", numeroInvalido, "Cidade", "ST", "12345-678"))
                .isInstanceOf(EnderecoValidationException.class)
                .hasMessage("O número do endereço é obrigatório.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Deve lançar exceção quando a cidade for inválida")
    void deveLancarexcecaoQuandoCidadeInvalida(String cidadeInvalida) {
        assertThatThrownBy(() -> new Endereco(1L, "Rua A", "123", cidadeInvalida, "ST", "12345-678"))
                .isInstanceOf(EnderecoValidationException.class)
                .hasMessage("A cidade do endereço é obrigatória.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Deve lançar exceção quando o estado for inválido")
    void deveLancarexcecaoQuandoEstadoInvalido(String estadoInvalido) {
        assertThatThrownBy(() -> new Endereco(1L, "Rua A", "123", "Cidade", estadoInvalido, "12345-678"))
                .isInstanceOf(EnderecoValidationException.class)
                .hasMessage("O estado do endereço é obrigatório.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("Deve lançar exceção quando o CEP for inválido")
    void deveLancarexcecaoQuandoCepInvalido(String cepInvalido) {
        assertThatThrownBy(() -> new Endereco(1L, "Rua A", "123", "Cidade", "ST", cepInvalido))
                .isInstanceOf(EnderecoValidationException.class)
                .hasMessage("O CEP do endereço é obrigatório.");
    }
}
