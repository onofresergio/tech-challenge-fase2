package br.com.fiap.techchallenge.infrastructure.database.adapter;

import br.com.fiap.techchallenge.domain.model.Restaurante;
import br.com.fiap.techchallenge.infrastructure.database.entity.RestauranteEntity;
import br.com.fiap.techchallenge.infrastructure.database.jpa.RestauranteJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestauranteRepositoryAdapterTest {

    @Mock
    private RestauranteJpaRepository restauranteJpaRepository;

    @InjectMocks
    private RestauranteRepositoryAdapter restauranteRepositoryAdapter;

    private final LocalDateTime horarioFuncionamento = LocalDateTime.of(2026, 10, 12, 18, 0);
    private final Restaurante restauranteDomain = new Restaurante(1L, "Pizzaria Fiap", 10L, 20L, horarioFuncionamento, 4L);

    // ==========================================
    // TESTES: save
    // ==========================================

    @Test
    @DisplayName("save: Deve converter o domínio para entidade, salvar no JPA e retornar o domínio correspondente")
    void saveDevePersistirERetornarModeloDeDominio() {
        // Arrange
        RestauranteEntity entityMock = mock(RestauranteEntity.class);
        when(entityMock.toDomain()).thenReturn(restauranteDomain);
        when(restauranteJpaRepository.save(any(RestauranteEntity.class))).thenReturn(entityMock);

        // Act
        Restaurante resultado = restauranteRepositoryAdapter.save(restauranteDomain);

        // Assert
        assertThat(resultado).isNotNull().isEqualTo(restauranteDomain);
        verify(restauranteJpaRepository, times(1)).save(any(RestauranteEntity.class));
    }

    // ==========================================
    // TESTES: findById
    // ==========================================

    @Test
    @DisplayName("findById: Deve retornar um Optional preenchido com o domínio quando o ID existir no JPA")
    void findByIdDeveRetornarOptionalComDominioQuandoExistir() {
        // Arrange
        RestauranteEntity entityMock = mock(RestauranteEntity.class);
        when(entityMock.toDomain()).thenReturn(restauranteDomain);
        when(restauranteJpaRepository.findById(1L)).thenReturn(Optional.of(entityMock));

        // Act
        Optional<Restaurante> resultado = restauranteRepositoryAdapter.findById(1L);

        // Assert
        assertThat(resultado).isPresent().contains(restauranteDomain);
        verify(restauranteJpaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById: Deve retornar um Optional vazio quando o ID não for encontrado no JPA")
    void findByIdDeveRetornarOptionalVazioQuandoNaoExistir() {
        // Arrange
        when(restauranteJpaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Restaurante> resultado = restauranteRepositoryAdapter.findById(1L);

        // Assert
        assertThat(resultado).isEmpty();
        verify(restauranteJpaRepository, times(1)).findById(1L);
    }

    // ==========================================
    // TESTES: findAll
    // ==========================================

    @Test
    @DisplayName("findAll: Deve retornar uma lista mapeada para o domínio quando houver registros cadastrados")
    void findAllDeveRetornarListaDeDominioPopulada() {
        // Arrange
        RestauranteEntity entityMock = mock(RestauranteEntity.class);
        when(entityMock.toDomain()).thenReturn(restauranteDomain);
        when(restauranteJpaRepository.findAll()).thenReturn(List.of(entityMock));

        // Act
        List<Restaurante> resultado = restauranteRepositoryAdapter.findAll();

        // Assert
        assertThat(resultado).isNotEmpty().hasSize(1).containsExactly(restauranteDomain);
        verify(restauranteJpaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll: Deve retornar uma lista vazia quando o repositório JPA não possuir dados")
    void findAllDeveRetornarListaVaziaQuandoNaoHouverDados() {
        // Arrange
        when(restauranteJpaRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Restaurante> resultado = restauranteRepositoryAdapter.findAll();

        // Assert
        assertThat(resultado).isEmpty();
        verify(restauranteJpaRepository, times(1)).findAll();
    }

    // ==========================================
    // TESTES: deleteById e existsById
    // ==========================================

    @Test
    @DisplayName("deleteById: Deve delegar a remoção por ID corretamente ao repositório JPA")
    void deleteByIdDeveInvocarRemocaoNoJpa() {
        // Act
        restauranteRepositoryAdapter.deleteById(1L);

        // Assert
        verify(restauranteJpaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("existsById: Deve repassar de forma transparente o retorno booleano do repositório JPA")
    void existsByIdDeveRetornarBooleanoDoJpa() {
        // Arrange
        when(restauranteJpaRepository.existsById(1L)).thenReturn(true);
        when(restauranteJpaRepository.existsById(2L)).thenReturn(false);

        // Act & Assert
        assertThat(restauranteRepositoryAdapter.existsById(1L)).isTrue();
        assertThat(restauranteRepositoryAdapter.existsById(2L)).isFalse();

        verify(restauranteJpaRepository, times(1)).existsById(1L);
        verify(restauranteJpaRepository, times(1)).existsById(2L);
    }

}
