package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.MenuNotFoundException;
import br.com.fiap.techchallenge.usecase.port.output.MenuRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeletarMenuInteractorTest {

    @Mock
    private MenuRepositoryPort menuRepositoryPort;

    @InjectMocks
    private DeletarMenuInteractor deletarMenuInteractor;

    @Test
    @DisplayName("execute: Deve deletar o menu com sucesso quando o ID existir no repositório")
    void executeDeveDeletarMenuQuandoIdExiste() {
        // Arrange
        Long menuId = 1L;
        when(menuRepositoryPort.existsById(menuId)).thenReturn(true);

        // Act
        deletarMenuInteractor.execute(menuId);

        // Assert
        verify(menuRepositoryPort, times(1)).existsById(menuId);
        verify(menuRepositoryPort, times(1)).deleteById(menuId);
    }

    @Test
    @DisplayName("execute: Deve lançar MenuNotFoundException e não deletar quando o ID não existir")
    void executeDeveLancarExcecaoEIdNaoDeletarQuandoIdNaoExiste() {
        // Arrange
        Long menuId = 1L;
        when(menuRepositoryPort.existsById(menuId)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> deletarMenuInteractor.execute(menuId))
                .isInstanceOf(MenuNotFoundException.class);

        // Verifica que a checagem de existência ocorreu, mas o método de remoção nunca foi chamado
        verify(menuRepositoryPort, times(1)).existsById(menuId);
        verify(menuRepositoryPort, never()).deleteById(anyLong());
    }

}
