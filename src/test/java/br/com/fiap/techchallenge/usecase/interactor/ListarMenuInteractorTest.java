package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.MenuNotFoundException;
import br.com.fiap.techchallenge.domain.model.Menu;
import br.com.fiap.techchallenge.usecase.port.output.MenuRepositoryPort;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListarMenuInteractorTest {
    @Mock
    private MenuRepositoryPort menuRepositoryPort;

    @InjectMocks
    private ListarMenuInteractor listarMenuInteractor;

    // ==========================================
    // TESTES: listar()
    // ==========================================

    @Test
    @DisplayName("listar: Deve retornar a lista completa de pratos do menu quando houver registros")
    void listarDeveRetornarListaDeMenus() {
        // Arrange
        Menu m1 = new Menu(1L, "Hambúrguer", "Blend artesanal de 150g", new BigDecimal("35.00"), true, "burger.jpg", 10L);
        Menu m2 = new Menu(2L, "Batata Frita", "Batatas rústicas com alecrim", new BigDecimal("18.00"), true, "fritas.jpg", 10L);
        List<Menu> listaEsperada = List.of(m1, m2);

        when(menuRepositoryPort.findAll()).thenReturn(listaEsperada);

        // Act
        List<Menu> resultado = listarMenuInteractor.listar();

        // Assert
        assertThat(resultado)
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(m1, m2);

        verify(menuRepositoryPort, times(1)).findAll();
    }

    @Test
    @DisplayName("listar: Deve retornar uma lista vazia quando nenhum prato estiver cadastrado")
    void listarDeveRetornarListaVaziaQuandoNaoHouverRegistros() {
        // Arrange
        when(menuRepositoryPort.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Menu> resultado = listarMenuInteractor.listar();

        // Assert
        assertThat(resultado).isEmpty();
        verify(menuRepositoryPort, times(1)).findAll();
    }

    // ==========================================
    // TESTES: buscarPorId(Long id)
    // ==========================================

    @Test
    @DisplayName("buscarPorId: Deve retornar o prato correto do menu quando o ID for encontrado")
    void buscarPorIdDeveRetornarMenuQuandoIdExistir() {
        // Arrange
        Long menuId = 1L;
        Menu menuEsperado = new Menu(menuId, "Hambúrguer", "Blend artesanal de 150g", new BigDecimal("35.00"), true, "burger.jpg", 10L);

        when(menuRepositoryPort.findById(menuId)).thenReturn(Optional.of(menuEsperado));

        // Act
        Menu resultado = listarMenuInteractor.buscarPorId(menuId);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEqualTo(menuEsperado);
        verify(menuRepositoryPort, times(1)).findById(menuId);
    }

    @Test
    @DisplayName("buscarPorId: Deve lançar MenuNotFoundException quando o ID não existir no repositório")
    void buscarPorIdDeveLancarExcecaoQuandoIdNaoExistir() {
        // Arrange
        Long menuId = 1L;
        when(menuRepositoryPort.findById(menuId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> listarMenuInteractor.buscarPorId(menuId))
                .isInstanceOf(MenuNotFoundException.class);

        verify(menuRepositoryPort, times(1)).findById(menuId);
    }
}
