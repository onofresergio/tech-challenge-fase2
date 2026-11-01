package br.com.fiap.techchallenge.infrastructure.database.entity;

import br.com.fiap.techchallenge.domain.model.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuEntityTest {
    private final Menu menuDomain = new Menu(
            1L,
            "Hambúrguer",
            "Blend artesanal de 150g",
            new BigDecimal("35.00"),
            true,
            "burger.jpg",
            10L
    );

    @Test
    @DisplayName("fromDomain: Deve mapear todas as propriedades do modelo de domínio para a entidade JPA com sucesso")
    void fromDomainDeveMapearTodasAsPropriedadesParaEntidade() {
        // Act
        MenuEntity entity = MenuEntity.fromDomain(menuDomain);

        // Assert
        assertThat(entity).isNotNull();
        // Usamos reflexão para extrair o valor dos campos privados da entidade
        assertThat(entity).extracting("id").isEqualTo(1L);
        assertThat(entity).extracting("nome").isEqualTo("Hambúrguer");
        assertThat(entity).extracting("descricao").isEqualTo("Blend artesanal de 150g");
        assertThat(entity).extracting("valorPrato").isEqualTo(new BigDecimal("35.00"));
        assertThat(entity).extracting("disponivelRestaurante").isEqualTo(true);
        assertThat(entity).extracting("fotoPrato").isEqualTo("burger.jpg");
        assertThat(entity).extracting("restauranteId").isEqualTo(10L);
    }

    @Test
    @DisplayName("toDomain: Deve mapear todas as propriedades da entidade JPA para o modelo de domínio com sucesso")
    void toDomainDeveMapearTodasAsPropriedadesParaModeloDeDominio() {
        // Arrange
        MenuEntity entity = MenuEntity.fromDomain(menuDomain);

        // Act
        Menu resultadoDomain = entity.toDomain();

        // Assert
        assertThat(resultadoDomain).isNotNull();
        assertThat(resultadoDomain.id()).isEqualTo(1L);
        assertThat(resultadoDomain.nome()).isEqualTo("Hambúrguer");
        assertThat(resultadoDomain.descricao()).isEqualTo("Blend artesanal de 150g");
        assertThat(resultadoDomain.valorPrato()).isEqualTo(new BigDecimal("35.00"));
        assertThat(resultadoDomain.disponivelRestaurante()).isTrue();
        assertThat(resultadoDomain.fotoPrato()).isEqualTo("burger.jpg");
        assertThat(resultadoDomain.restauranteId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("Constructor: Deve instanciar a entidade através do construtor padrão vazio do JPA")
    void deveGarantirCoberturaDoConstrutorPadraoVazio() {
        // Act
        MenuEntity entity = new MenuEntity();

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity).extracting("id").isNull();
        assertThat(entity).extracting("nome").isNull();
    }
}
