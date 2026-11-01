package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.RestauranteNotFoundException;
import br.com.fiap.techchallenge.usecase.port.output.RestauranteRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeletarRestauranteInteractorTest {

    @Mock
    private RestauranteRepositoryPort restauranteRepositoryPort;

    @InjectMocks
    private DeletarRestauranteInteractor deletarRestauranteInteractor;

    @Test
    @DisplayName("execute: Deve deletar o restaurante com sucesso quando o ID existir no repositório")
    void executeDeveDeletarRestauranteQuandoIdExiste() {
        // Arrange
        Long restauranteId = 1L;
        when(restauranteRepositoryPort.existsById(restauranteId)).thenReturn(true);

        // Act
        deletarRestauranteInteractor.execute(restauranteId);

        // Assert
        verify(restauranteRepositoryPort, times(1)).existsById(restauranteId);
        verify(restauranteRepositoryPort, times(1)).deleteById(restauranteId);
    }

    @Test
    @DisplayName("execute: Deve lançar RestauranteNotFoundException e não deletar quando o ID não existir")
    void executeDeveLancarExcecaoEIdNaoDeletarQuandoIdNaoExiste() {
        // Arrange
        Long restauranteId = 1L;
        when(restauranteRepositoryPort.existsById(restauranteId)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> deletarRestauranteInteractor.execute(restauranteId))
                .isInstanceOf(RestauranteNotFoundException.class);

        // Verifica que a verificação de existência ocorreu, mas a exclusão nunca foi chamada
        verify(restauranteRepositoryPort, times(1)).existsById(restauranteId);
        verify(restauranteRepositoryPort, never()).deleteById(anyLong());
    }

}
