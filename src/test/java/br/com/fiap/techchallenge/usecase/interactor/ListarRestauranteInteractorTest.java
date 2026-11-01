package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.RestauranteNotFoundException;
import br.com.fiap.techchallenge.domain.model.Restaurante;
import br.com.fiap.techchallenge.usecase.port.output.RestauranteRepositoryPort;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListarRestauranteInteractorTest {
    @Mock
    private RestauranteRepositoryPort restauranteRepositoryPort;

    @InjectMocks
    private ListarRestauranteInteractor listarRestauranteInteractor;

    private final LocalDateTime horarioFuncionamento = LocalDateTime.of(2026, 10, 12, 18, 0);

    // ==========================================
    // TESTES: listar()
    // ==========================================

    @Test
    @DisplayName("listar: Deve retornar uma lista preenchida de restaurantes quando existirem registros")
    void listarDeveRetornarListaDeRestaurantes() {
        // Arrange
        Restaurante r1 = new Restaurante(1L, "Restaurante A", 10L, 20L, horarioFuncionamento, 4L);
        Restaurante r2 = new Restaurante(2L, "Restaurante B", 11L, 21L, horarioFuncionamento, 4L);
        List<Restaurante> listaEsperada = List.of(r1, r2);

        when(restauranteRepositoryPort.findAll()).thenReturn(listaEsperada);

        // Act
        List<Restaurante> resultado = listarRestauranteInteractor.listar();

        // Assert
        assertThat(resultado)
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(r1, r2);

        verify(restauranteRepositoryPort, times(1)).findAll();
    }

    @Test
    @DisplayName("listar: Deve retornar uma lista vazia quando não houver restaurantes cadastrados")
    void listarDeveRetornarListaVaziaQuandoNaoHouverRegistros() {
        // Arrange
        when(restauranteRepositoryPort.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Restaurante> resultado = listarRestauranteInteractor.listar();

        // Assert
        assertThat(resultado).isEmpty();
        verify(restauranteRepositoryPort, times(1)).findAll();
    }

    // ==========================================
    // TESTES: buscarPorId(Long id)
    // ==========================================

    @Test
    @DisplayName("buscarPorId: Deve retornar o restaurante correto quando o ID for encontrado")
    void buscarPorIdDeveRetornarRestauranteQuandoIdExistir() {
        // Arrange
        Long restauranteId = 1L;
        Restaurante restauranteEsperado = new Restaurante(restauranteId, "Restaurante A", 10L, 20L, horarioFuncionamento, 4L);

        when(restauranteRepositoryPort.findById(restauranteId)).thenReturn(Optional.of(restauranteEsperado));

        // Act
        Restaurante resultado = listarRestauranteInteractor.buscarPorId(restauranteId);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEqualTo(restauranteEsperado);
        verify(restauranteRepositoryPort, times(1)).findById(restauranteId);
    }

    @Test
    @DisplayName("buscarPorId: Deve lançar RestauranteNotFoundException quando o ID não existir no repositório")
    void buscarPorIdDeveLancarExcecaoQuandoIdNaoExistir() {
        // Arrange
        Long restauranteId = 1L;
        when(restauranteRepositoryPort.findById(restauranteId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> listarRestauranteInteractor.buscarPorId(restauranteId))
                .isInstanceOf(RestauranteNotFoundException.class);

        verify(restauranteRepositoryPort, times(1)).findById(restauranteId);
    }
}
