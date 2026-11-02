package br.com.fiap.techchallenge.infrastructure.database.entity;

import br.com.fiap.techchallenge.domain.model.Restaurante;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class RestauranteEntityTest {
    private final LocalDateTime horarioFuncionamento = LocalDateTime.of(2026, 10, 12, 18, 0);

    private final Restaurante restauranteDomain = new Restaurante(
            1L,
            "Sabor das Américas",
            10L,
            20L,
            horarioFuncionamento,
            4L
    );

    @Test
    @DisplayName("fromDomain: Deve mapear todas as propriedades do modelo de domínio para a entidade JPA com sucesso")
    void fromDomainDeveMapearTodasAsPropriedadesParaEntidade() {
        // Act
        RestauranteEntity entity = RestauranteEntity.fromDomain(restauranteDomain);

        // Assert
        assertThat(entity).isNotNull();
        // Utilizamos reflexão (extracting) para validar os campos privados da entidade mapeada
        assertThat(entity).extracting("id").isEqualTo(1L);
        assertThat(entity).extracting("nome").isEqualTo("Sabor das Américas");
        assertThat(entity).extracting("enderecoId").isEqualTo(10L);
        assertThat(entity).extracting("tipoCozinhaId").isEqualTo(20L);
        assertThat(entity).extracting("horarioFuncionamento").isEqualTo(horarioFuncionamento);
        assertThat(entity).extracting("usuarioId").isEqualTo(4L);
    }

    @Test
    @DisplayName("toDomain: Deve mapear todas as propriedades da entidade JPA de volta para o modelo de domínio com sucesso")
    void toDomainDeveMapearTodasAsPropriedadesParaModeloDeDominio() {
        // Arrange
        RestauranteEntity entity = RestauranteEntity.fromDomain(restauranteDomain);

        // Act
        Restaurante resultadoDomain = entity.toDomain();

        // Assert
        assertThat(resultadoDomain).isNotNull();
        assertThat(resultadoDomain.id()).isEqualTo(1L);
        assertThat(resultadoDomain.nome()).isEqualTo("Sabor das Américas");
        assertThat(resultadoDomain.enderecoId()).isEqualTo(10L);
        assertThat(resultadoDomain.tipoCozinhaId()).isEqualTo(20L);
        assertThat(resultadoDomain.horarioFuncionamento()).isEqualTo(horarioFuncionamento);
        assertThat(resultadoDomain.usuarioId()).isEqualTo(4L);
    }

    @Test
    @DisplayName("Constructor: Deve garantir a cobertura do construtor padrão vazio exigido pela especificação JPA")
    void deveGarantirCoberturaDoConstrutorPadraoVazio() {
        // Act
        RestauranteEntity entity = new RestauranteEntity();

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity).extracting("id").isNull();
        assertThat(entity).extracting("nome").isNull();
        assertThat(entity).extracting("horarioFuncionamento").isNull();
    }
}
