package br.com.fiap.techchallenge.infrastructure.database.entity;

import br.com.fiap.techchallenge.domain.model.TipoUsuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TipoUsuarioEntityTest {

    private final TipoUsuario tipoUsuarioDomain = new TipoUsuario(1L, "DONO_RESTAURANTE");

    @Test
    @DisplayName("fromDomain: Deve mapear todas as propriedades do modelo de domínio para a entidade JPA com sucesso")
    void fromDomainDeveMapearTodasAsPropriedadesParaEntidade() {
        // Act
        TipoUsuarioEntity entity = TipoUsuarioEntity.fromDomain(tipoUsuarioDomain);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getNome()).isEqualTo("DONO_RESTAURANTE");
    }

    @Test
    @DisplayName("toDomain: Deve mapear todas as propriedades da entidade JPA de volta para o modelo de domínio com sucesso")
    void toDomainDeveMapearTodasAsPropriedadesParaModeloDeDominio() {
        // Arrange
        TipoUsuarioEntity entity = TipoUsuarioEntity.fromDomain(tipoUsuarioDomain);

        // Act
        TipoUsuario resultadoDomain = entity.toDomain();

        // Assert
        assertThat(resultadoDomain).isNotNull();
        assertThat(resultadoDomain.id()).isEqualTo(1L);
        assertThat(resultadoDomain.nome()).isEqualTo("DONO_RESTAURANTE");
    }

    @Test
    @DisplayName("Constructor: Deve garantir a cobertura do construtor padrão vazio exigido pelo Hibernate/JPA")
    void deveGarantirCoberturaDoConstrutorPadraoVazio() {
        // Act
        TipoUsuarioEntity entity = new TipoUsuarioEntity();

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getNome()).isNull();
    }
}
