package br.com.fiap.techchallenge.infrastructure.database.adapter;

import br.com.fiap.techchallenge.domain.model.Menu;
import br.com.fiap.techchallenge.infrastructure.database.entity.MenuEntity;
import br.com.fiap.techchallenge.infrastructure.database.jpa.MenuJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuRepositoryAdapterTest {
    @Mock
    private MenuJpaRepository menuJpaRepository;

    @InjectMocks
    private MenuRepositoryAdapter menuRepositoryAdapter;

    private final Menu menuDomain = new Menu(1L, "Burguer", "Artesanal", new BigDecimal("35.00"), true, "foto.jpg", 10L);

    // ==========================================
    // TESTES: save
    // ==========================================

    @Test
    @DisplayName("save: Deve converter o modelo de domínio para entidade, salvar no JPA e retornar o domínio convertido")
    void saveDevePersistirERetornarModeloDeDominio() {
        // Arrange
        MenuEntity entityMock = mock(MenuEntity.class);
        when(entityMock.toDomain()).thenReturn(menuDomain);
        when(menuJpaRepository.save(any(MenuEntity.class))).thenReturn(entityMock);

        // Act
        Menu resultado = menuRepositoryAdapter.save(menuDomain);

        // Assert
        assertThat(resultado).isNotNull().isEqualTo(menuDomain);
        verify(menuJpaRepository, times(1)).save(any(MenuEntity.class));
    }

    // ==========================================
    // TESTES: findById
    // ==========================================

    @Test
    @DisplayName("findById: Deve retornar um Optional contendo o domínio quando o registro existir no JPA")
    void findByIdDeveRetornarOptionalComDominioQuandoExistir() {
        // Arrange
        MenuEntity entityMock = mock(MenuEntity.class);
        when(entityMock.toDomain()).thenReturn(menuDomain);
        when(menuJpaRepository.findById(1L)).thenReturn(Optional.of(entityMock));

        // Act
        Optional<Menu> resultado = menuRepositoryAdapter.findById(1L);

        // Assert
        assertThat(resultado).isPresent().contains(menuDomain);
        verify(menuJpaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById: Deve retornar um Optional vazio quando o registro não existir no JPA")
    void findByIdDeveRetornarOptionalVazioQuandoNaoExistir() {
        // Arrange
        when(menuJpaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Menu> resultado = menuRepositoryAdapter.findById(1L);

        // Assert
        assertThat(resultado).isEmpty();
        verify(menuJpaRepository, times(1)).findById(1L);
    }

    // ==========================================
    // TESTES: findAll
    // ==========================================

    @Test
    @DisplayName("findAll: Deve retornar a lista mapeada para domínio quando houver registros")
    void findAllDeveRetornarListaDeDominioPopulada() {
        // Arrange
        MenuEntity entityMock = mock(MenuEntity.class);
        when(entityMock.toDomain()).thenReturn(menuDomain);
        when(menuJpaRepository.findAll()).thenReturn(List.of(entityMock));

        // Act
        List<Menu> resultado = menuRepositoryAdapter.findAll();

        // Assert
        assertThat(resultado).hasSize(1).containsExactly(menuDomain);
        verify(menuJpaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll: Deve retornar uma lista vazia quando o JPA não possuir registros")
    void findAllDeveRetornarListaVazia() {
        // Arrange
        when(menuJpaRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Menu> resultado = menuRepositoryAdapter.findAll();

        // Assert
        assertThat(resultado).isEmpty();
        verify(menuJpaRepository, times(1)).findAll();
    }

    // ==========================================
    // TESTES: findAllByRestauranteId
    // ==========================================

    @Test
    @DisplayName("findAllByRestauranteId: Deve filtrar e retornar a lista mapeada por restaurante")
    void findAllByRestauranteIdDeveRetornarListaFiltrada() {
        // Arrange
        Long restauranteId = 10L;
        MenuEntity entityMock = mock(MenuEntity.class);
        when(entityMock.toDomain()).thenReturn(menuDomain);
        when(menuJpaRepository.findByRestauranteId(restauranteId)).thenReturn(List.of(entityMock));

        // Act
        List<Menu> resultado = menuRepositoryAdapter.findAllByRestauranteId(restauranteId);

        // Assert
        assertThat(resultado).hasSize(1).containsExactly(menuDomain);
        verify(menuJpaRepository, times(1)).findByRestauranteId(restauranteId);
    }

    // ==========================================
    // TESTES: deleteById e existsById
    // ==========================================

    @Test
    @DisplayName("deleteById: Deve invocar a deleção no repositório JPA corretamente")
    void deleteByIdDeveInvocarRemocaoNoJpa() {
        // Act
        menuRepositoryAdapter.deleteById(1L);

        // Assert
        verify(menuJpaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("existsById: Deve retornar o booleano condizente com a resposta do repositório JPA")
    void existsByIdDeveRetornarBooleanoDoJpa() {
        // Arrange
        when(menuJpaRepository.existsById(1L)).thenReturn(true);
        when(menuJpaRepository.existsById(2L)).thenReturn(false);

        // Act & Assert
        assertThat(menuRepositoryAdapter.existsById(1L)).isTrue();
        assertThat(menuRepositoryAdapter.existsById(2L)).isFalse();

        verify(menuJpaRepository, times(1)).existsById(1L);
        verify(menuJpaRepository, times(1)).existsById(2L);
    }
}
