package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.MenuNotFoundException;
import br.com.fiap.techchallenge.domain.model.Menu;
import br.com.fiap.techchallenge.usecase.port.output.MenuRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AtualizarMenuInteractorTest {

    @Mock
    private MenuRepositoryPort menuRepositoryPort;

    @InjectMocks
    private AtualizarMenuInteractor atualizarMenuInteractor;

    @Test
    @DisplayName("execute: Deve atualizar e salvar o menu com sucesso quando o prato existir")
    void executeDeveAtualizarComSucessoQuandoMenuExistir() {
        // Arrange
        Long menuId = 1L;
        Long restauranteIdImutavel = 10L;

        Menu menuAtual = new Menu(menuId, "Nome Antigo", "Descricao Antiga", new BigDecimal("30.00"), true, "foto_antiga.jpg", restauranteIdImutavel);
        Menu menuSalvo = new Menu(menuId, "Nome Novo", "Descricao Nova", new BigDecimal("45.50"), false, "foto_nova.jpg", restauranteIdImutavel);

        when(menuRepositoryPort.findById(menuId)).thenReturn(Optional.of(menuAtual));
        when(menuRepositoryPort.save(any(Menu.class))).thenReturn(menuSalvo);

        // Act
        Menu resultado = atualizarMenuInteractor.execute(
                menuId,
                "Nome Novo",
                "Descricao Nova",
                new BigDecimal("45.50"),
                false,
                "foto_nova.jpg"
        );

        // Assert
        assertThat(resultado).isEqualTo(menuSalvo);

        // Captura o objeto para garantir que o restauranteId original foi preservado na criação do novo Record
        ArgumentCaptor<Menu> menuCaptor = ArgumentCaptor.forClass(Menu.class);
        verify(menuRepositoryPort, times(1)).save(menuCaptor.capture());

        Menu enviadoParaSalvar = menuCaptor.getValue();
        assertThat(enviadoParaSalvar.id()).isEqualTo(menuId);
        assertThat(enviadoParaSalvar.nome()).isEqualTo("Nome Novo");
        assertThat(enviadoParaSalvar.descricao()).isEqualTo("Descricao Nova");
        assertThat(enviadoParaSalvar.valorPrato()).isEqualTo(new BigDecimal("45.50"));
        assertThat(enviadoParaSalvar.disponivelRestaurante()).isFalse();
        assertThat(enviadoParaSalvar.fotoPrato()).isEqualTo("foto_nova.jpg");
        assertThat(enviadoParaSalvar.restauranteId()).isEqualTo(restauranteIdImutavel); // Preservado
    }

    @Test
    @DisplayName("execute: Deve lançar MenuNotFoundException quando o prato de menu não for encontrado")
    void executeDeveLancarExcecaoQuandoMenuNaoEncontrado() {
        // Arrange
        Long menuId = 1L;
        when(menuRepositoryPort.findById(menuId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> atualizarMenuInteractor.execute(
                menuId,
                "Nome Novo",
                "Descricao Nova",
                new BigDecimal("45.50"),
                true,
                "foto_nova.jpg"
        )).isInstanceOf(MenuNotFoundException.class);

        // Garante que o repositório nunca tentou salvar dados após a falha de validação
        verify(menuRepositoryPort, never()).save(any());
    }

}
