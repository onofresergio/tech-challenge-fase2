package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.RestauranteNotFoundException;
import br.com.fiap.techchallenge.domain.exception.UnauthorizedUserException;
import br.com.fiap.techchallenge.domain.model.Restaurante;
import br.com.fiap.techchallenge.usecase.port.output.RestauranteRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AtualizarRestauranteInteractorTest {

    @Mock
    private RestauranteRepositoryPort restauranteRepositoryPort;

    @InjectMocks
    private AtualizarRestauranteInteractor atualizarRestauranteInteractor;

    private final LocalDateTime horarioAntigo = LocalDateTime.of(2026, 10, 12, 12, 0);
    private final LocalDateTime horarioNovo = LocalDateTime.of(2026, 10, 12, 18, 0);

    @Test
    @DisplayName("execute: Deve atualizar e salvar o restaurante com sucesso quando o usuário for autorizado")
    void executeDeveAtualizarComSucessoQuandoUsuarioAutorizado() {
        // Arrange
        Long restauranteId = 1L;
        Long tipoUsuarioIdAutorizado = 4L;

        Restaurante restauranteAtual = new Restaurante(restauranteId, "Nome Antigo", 2L, 3L, horarioAntigo, tipoUsuarioIdAutorizado);
        Restaurante restauranteSalvo = new Restaurante(restauranteId, "Nome Novo", 2L, 3L, horarioNovo, tipoUsuarioIdAutorizado);

        when(restauranteRepositoryPort.findById(restauranteId)).thenReturn(Optional.of(restauranteAtual));
        // CORREÇÃO: Utilizando a sintaxe limpa e correta do ArgumentMatchers.any()
        when(restauranteRepositoryPort.save(any(Restaurante.class))).thenReturn(restauranteSalvo);

        // Act
        Restaurante resultado = atualizarRestauranteInteractor.execute(restauranteId, "Nome Novo", 2L, 3L, horarioNovo, tipoUsuarioIdAutorizado);

        // Assert
        assertThat(resultado).isEqualTo(restauranteSalvo);

        // Captura o objeto mapeado para garantir que dados imutáveis (como enderecoId e tipoCozinhaId) não foram alterados
        ArgumentCaptor<Restaurante> restauranteCaptor = ArgumentCaptor.forClass(Restaurante.class);
        verify(restauranteRepositoryPort, times(1)).save(restauranteCaptor.capture());

        Restaurante enviadoParaSalvar = restauranteCaptor.getValue();
        assertThat(enviadoParaSalvar.id()).isEqualTo(restauranteId);
        assertThat(enviadoParaSalvar.nome()).isEqualTo("Nome Novo");
        assertThat(enviadoParaSalvar.horarioFuncionamento()).isEqualTo(horarioNovo);
        assertThat(enviadoParaSalvar.enderecoId()).isEqualTo(2L);
        assertThat(enviadoParaSalvar.tipoCozinhaId()).isEqualTo(3L);
        assertThat(enviadoParaSalvar.usuarioId()).isEqualTo(tipoUsuarioIdAutorizado);
    }

    @Test
    @DisplayName("execute: Deve lançar RestauranteNotFoundException quando o restaurante não existir")
    void executeDeveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        // Arrange
        Long restauranteId = 1L;
        when(restauranteRepositoryPort.findById(restauranteId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> atualizarRestauranteInteractor.execute(restauranteId, "Nome Novo", 1L, 1L, horarioNovo, 4L))
                .isInstanceOf(RestauranteNotFoundException.class);

        verify(restauranteRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("execute: Deve lançar UnauthorizedUserException quando o tipoUsuarioId for diferente do dono do restaurante")
    void executeDeveLancarExcecaoQuandoUsuarioNaoForAutorizado() {
        // Arrange
        Long restauranteId = 1L;
        Long tipoUsuarioDono = 4L;
        Long tipoUsuarioInvasor = 99L; // IDs diferentes ativam o bloco 'if (!Objects.equals(...))'

        Restaurante restauranteAtual = new Restaurante(restauranteId, "Sabor Real", 2L, 3L, horarioAntigo, tipoUsuarioDono);
        when(restauranteRepositoryPort.findById(restauranteId)).thenReturn(Optional.of(restauranteAtual));

        // Act & Assert
        assertThatThrownBy(() -> atualizarRestauranteInteractor.execute(restauranteId, "Nome Novo", 1L, 1L, horarioNovo, tipoUsuarioInvasor))
                .isInstanceOf(UnauthorizedUserException.class);

        // Garante a segurança do Clean Architecture: se não for autorizado, o repositório nunca deve ser invocado para salvar
        verify(restauranteRepositoryPort, never()).save(any());
    }
}
