package br.com.fiap.techchallenge.usecase.interactor;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CriarMenuInteractorTest {

    @Mock
    private MenuRepositoryPort menuRepositoryPort;

    @InjectMocks
    private CriarMenuInteractor criarMenuInteractor;

    @Test
    @DisplayName("execute: Deve criar e salvar um prato do menu com sucesso")
    void executeDeveCriarESalvarMenuComSucesso() {
        // Arrange
        String nome = "Hambúrguer";
        String descricao = "Blend artesanal de 150g";
        BigDecimal valorPrato = new BigDecimal("35.00");
        boolean disponivelRestaurante = true;
        String fotoPrato = "burger.jpg";
        Long restauranteId = 10L;

        Menu menuSalvo = new Menu(1L, nome, descricao, valorPrato, disponivelRestaurante, fotoPrato, restauranteId);

        when(menuRepositoryPort.save(any(Menu.class))).thenReturn(menuSalvo);

        // Act
        Menu resultado = criarMenuInteractor.execute(nome, descricao, valorPrato, disponivelRestaurante, fotoPrato, restauranteId);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEqualTo(menuSalvo);

        // Captura o argumento enviado ao repositório para validar o estado interno do Record criado
        ArgumentCaptor<Menu> menuCaptor = ArgumentCaptor.forClass(Menu.class);
        verify(menuRepositoryPort, times(1)).save(menuCaptor.capture());

        Menu enviadoParaSalvar = menuCaptor.getValue();
        assertThat(enviadoParaSalvar.id()).isNull(); // O ID deve ser obrigatoriamente nulo na criação
        assertThat(enviadoParaSalvar.nome()).isEqualTo(nome);
        assertThat(enviadoParaSalvar.descricao()).isEqualTo(descricao);
        assertThat(enviadoParaSalvar.valorPrato()).isEqualTo(valorPrato);
        assertThat(enviadoParaSalvar.disponivelRestaurante()).isTrue();
        assertThat(enviadoParaSalvar.fotoPrato()).isEqualTo(fotoPrato);
        assertThat(enviadoParaSalvar.restauranteId()).isEqualTo(restauranteId);
    }

}
