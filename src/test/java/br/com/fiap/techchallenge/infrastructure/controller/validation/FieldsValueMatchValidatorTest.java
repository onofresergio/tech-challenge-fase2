package br.com.fiap.techchallenge.infrastructure.controller.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FieldsValueMatchValidatorTest {
    private FieldsValueMatchValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private FieldsValueMatch constraintAnnotation;

    @BeforeEach
    void setUp() {
        validator = new FieldsValueMatchValidator();
        // Configura o mock da anotação para indicar quais campos devem ser comparados por reflexão
        when(constraintAnnotation.field()).thenReturn("senha");
        when(constraintAnnotation.fieldMatch()).thenReturn("confirmarSenha");
        validator.initialize(constraintAnnotation);
    }

    @Test
    @DisplayName("isValid: Deve retornar true quando ambos os campos forem iguais e preenchidos")
    void deveRetornarTrueQuandoCamposForemIguais() {
        // Arrange
        UsuarioMockForm form = new UsuarioMockForm("senha123", "senha123");

        // Act
        boolean resultado = validator.isValid(form, context);

        // Assert
        assertThat(resultado).isTrue();
    }

    @Test
    @DisplayName("isValid: Deve retornar false quando os campos forem diferentes")
    void deveRetornarFalseQuandoCamposForemDiferentes() {
        // Arrange
        UsuarioMockForm form = new UsuarioMockForm("senha123", "outraSenha");

        // Act
        boolean resultado = validator.isValid(form, context);

        // Assert
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("isValid: Deve retornar false quando o primeiro campo for nulo e o segundo preenchido")
    void deveRetornarFalseQuandoPrimeiroCampoForNulo() {
        // Arrange
        UsuarioMockForm form = new UsuarioMockForm(null, "senha123");

        // Act
        boolean resultado = validator.isValid(form, context);

        // Assert
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("isValid: Deve retornar true quando ambos os campos forem nulos")
    void deveRetornarTrueQuandoAmbosOsCamposForemNulos() {
        // Arrange
        UsuarioMockForm form = new UsuarioMockForm(null, null);

        // Act
        boolean resultado = validator.isValid(form, context);

        // Assert
        assertThat(resultado).isTrue();
    }

    // ==========================================
    // CLASSE AUXILIAR PARA O TESTE (STUB)
    // ==========================================
    private static class UsuarioMockForm {
        private final String senha;
        private final String confirmarSenha;

        public UsuarioMockForm(String senha, String confirmarSenha) {
            this.senha = senha;
            this.confirmarSenha = confirmarSenha;
        }

        // Getters necessários para o BeanWrapperImpl conseguir ler os campos via reflexão
        public String getSenha() {
            return senha;
        }

        public String getConfirmarSenha() {
            return confirmarSenha;
        }
    }
}
