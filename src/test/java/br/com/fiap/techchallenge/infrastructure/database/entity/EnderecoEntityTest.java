package br.com.fiap.techchallenge.infrastructure.database.entity;


import br.com.fiap.techchallenge.domain.model.Endereco;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
public class EnderecoEntityTest {

    private final Endereco enderecoDomain = new Endereco(
            1L,
            "Avenida Paulista",
            "1500",
            "São Paulo",
            "SP",
            "01310-100"
    );

    @Test
    @DisplayName("fromDomain: Deve mapear todas as propriedades do modelo de domínio para a entidade JPA com sucesso")
    void fromDomainDeveMapearTodasAsPropriedadesParaEntidade() {
        // Act
        EnderecoEntity entity = EnderecoEntity.fromDomain(enderecoDomain);

        // Assert
        assertThat(entity).isNotNull();
        // Usamos reflexão (extracting) para validar os campos privados da entidade mapeada
        assertThat(entity).extracting("id").isEqualTo(1L);
        assertThat(entity).extracting("rua").isEqualTo("Avenida Paulista");
        assertThat(entity).extracting("numero").isEqualTo("1500");
        assertThat(entity).extracting("cidade").isEqualTo("São Paulo");
        assertThat(entity).extracting("estado").isEqualTo("SP");
        assertThat(entity).extracting("cep").isEqualTo("01310-100");
    }

    @Test
    @DisplayName("toDomain: Deve mapear todas as propriedades da entidade JPA de volta para o modelo de domínio com sucesso")
    void toDomainDeveMapearTodasAsPropriedadesParaModeloDeDominio() {
        // Arrange
        EnderecoEntity entity = EnderecoEntity.fromDomain(enderecoDomain);

        // Act
        Endereco resultadoDomain = entity.toDomain();

        // Assert
        assertThat(resultadoDomain).isNotNull();
        assertThat(resultadoDomain.id()).isEqualTo(1L);
        assertThat(resultadoDomain.rua()).isEqualTo("Avenida Paulista");
        assertThat(resultadoDomain.numero()).isEqualTo("1500");
        assertThat(resultadoDomain.cidade()).isEqualTo("São Paulo");
        assertThat(resultadoDomain.estado()).isEqualTo("SP");
        assertThat(resultadoDomain.cep()).isEqualTo("01310-100");
    }

    @Test
    @DisplayName("Constructor: Deve garantir a cobertura do construtor padrão vazio exigido pelo Hibernate/JPA")
    void deveGarantirCoberturaDoConstrutorPadraoVazio() {
        // Act
        EnderecoEntity entity = new EnderecoEntity();

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity).extracting("id").isNull();
        assertThat(entity).extracting("rua").isNull();
        assertThat(entity).extracting("cep").isNull();
    }
}
