package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.UnauthorizedUserException;
import br.com.fiap.techchallenge.domain.model.Restaurante;
import br.com.fiap.techchallenge.usecase.port.output.RestauranteRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioAuthorizationPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CriarRestauranteInteractorTest {

    @Mock
    private RestauranteRepositoryPort restauranteRepositoryPort;

    @Mock
    private TipoUsuarioAuthorizationPort tipoUsuarioAuthorizationPort;

    @InjectMocks
    private CriarRestauranteInteractor criarRestauranteInteractor;

    private final LocalDateTime horarioFuncionamento = LocalDateTime.of(2026, 10, 12, 18, 0);

    @Test
    @DisplayName("execute: Deve criar e salvar o restaurante com sucesso quando o usuário for autorizado")
    void executeDeveCriarRestauranteComSucessoQuandoUsuarioAutorizado() {
        // Arrange
        String nome = "Sabor da América";
        Long enderecoId = 10L;
        Long tipoCozinhaId = 20L;
        Long tipoUsuarioIdDono = 4L;

        Restaurante restauranteSalvo = new Restaurante(1L, nome, enderecoId, tipoCozinhaId, horarioFuncionamento, tipoUsuarioIdDono);

        when(tipoUsuarioAuthorizationPort.isRestaurantOwner(tipoUsuarioIdDono)).thenReturn(true);
        when(restauranteRepositoryPort.save(any(Restaurante.class))).thenReturn(restauranteSalvo);

        // Act
        Restaurante resultado = criarRestauranteInteractor.execute(nome, enderecoId, tipoCozinhaId, horarioFuncionamento, tipoUsuarioIdDono);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEqualTo(restauranteSalvo);

        // Captura o argumento enviado para salvar para validar as invariantes do Record
        ArgumentCaptor<Restaurante> restauranteCaptor = ArgumentCaptor.forClass(Restaurante.class);
        verify(restauranteRepositoryPort, times(1)).save(restauranteCaptor.capture());

        Restaurante enviadoParaSalvar = restauranteCaptor.getValue();
        assertThat(enviadoParaSalvar.id()).isNull(); // ID obrigatoriamente nulo no fluxo de criação
        assertThat(enviadoParaSalvar.nome()).isEqualTo(nome);
        assertThat(enviadoParaSalvar.enderecoId()).isEqualTo(enderecoId);
        assertThat(enviadoParaSalvar.tipoCozinhaId()).isEqualTo(tipoCozinhaId);
        assertThat(enviadoParaSalvar.horarioFuncionamento()).isEqualTo(horarioFuncionamento);
        assertThat(enviadoParaSalvar.tipoUsuarioId()).isEqualTo(tipoUsuarioIdDono);
    }

    @Test
    @DisplayName("execute: Deve lançar UnauthorizedUserException e não salvar quando o usuário não for dono de restaurante")
    void executeDeveLancarExcecaoQuandoUsuarioNaoForAutorizado() {
        // Arrange
        Long tipoUsuarioInvalido = 99L;
        when(tipoUsuarioAuthorizationPort.isRestaurantOwner(tipoUsuarioInvalido)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> criarRestauranteInteractor.execute(
                "Restaurante Teste",
                10L,
                20L,
                horarioFuncionamento,
                tipoUsuarioInvalido
        )).isInstanceOf(UnauthorizedUserException.class);

        // Segurança da arquitetura: garante que o método save nunca foi invocado se a checagem falhar
        verify(restauranteRepositoryPort, never()).save(any());
    }

}
