package br.com.fiap.techchallenge.infrastructure.database.entity;

import br.com.fiap.techchallenge.domain.model.TipoCozinha;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TipoCozinhaEntityTest {

    private final TipoCozinha tipoCozinhaDomain = new TipoCozinha(1L, "Italiana");

    @Test
    @DisplayName("fromDomain: Deve mapear todas as propriedades do modelo de domínio para a entidade JPA com sucesso")
    void fromDomainDeveMapearTodasAsPropriedadesParaEntidade() {
        // Act
        TipoCozinhaEntity entity = TipoCozinhaEntity.fromDomain(tipoCozinhaDomain);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getNome()).isEqualTo("Italiana");
    }

    @Test
    @DisplayName("toDomain: Deve mapear todas as propriedades da entidade JPA de volta para o modelo de domínio com sucesso")
    void toDomainDeveMapearTodasAsPropriedadesParaModeloDeDominio() {
        // Arrange
        TipoCozinhaEntity entity = TipoCozinhaEntity.fromDomain(tipoCozinhaDomain);

        // Act
        TipoCozinha resultadoDomain = entity.toDomain();

        // Assert
        assertThat(resultadoDomain).isNotNull();
        assertThat(resultadoDomain.id()).isEqualTo(1L);
        assertThat(resultadoDomain.nome()).isEqualTo("Italiana");
    }

    @Test
    @DisplayName("Constructor: Deve garantir a cobertura do construtor padrão vazio exigido pela especificação JPA")
    void deveGarantirCoberturaDoConstrutorPadraoVazio() {
        // Act
        TipoCozinhaEntity entity = new TipoCozinhaEntity();

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getNome()).isNull();
    }
}
