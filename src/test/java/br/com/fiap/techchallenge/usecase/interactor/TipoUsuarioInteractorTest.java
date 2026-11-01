package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.TipoUsuarioNotFoundException;
import br.com.fiap.techchallenge.domain.exception.TipoUsuarioValidationException;
import br.com.fiap.techchallenge.domain.model.TipoUsuario;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TipoUsuarioInteractorTest {

    @Mock
    private TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort;

    @InjectMocks
    private TipoUsuarioInteractor tipoUsuarioInteractor;

    // ==========================================
    // TESTES: update
    // ==========================================

    @Test
    @DisplayName("update: Deve atualizar e salvar o tipo de usuário com sucesso se ele existir")
    void updateDeveSalvarComSucessoQuandoIdExiste() {
        // Arrange
        TipoUsuario tipoUsuarioEsperado = new TipoUsuario(1L, "ADMINISTRADOR_ALTERADO");
        when(tipoUsuarioRepositoryPort.existsById(1L)).thenReturn(true);
        when(tipoUsuarioRepositoryPort.save(any(TipoUsuario.class))).thenReturn(tipoUsuarioEsperado);

        // Act
        TipoUsuario resultado = tipoUsuarioInteractor.update(1L, "ADMINISTRADOR_ALTERADO");

        // Assert
        assertThat(resultado).isEqualTo(tipoUsuarioEsperado);
        verify(tipoUsuarioRepositoryPort, times(1)).save(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("update: Deve lançar TipoUsuarioNotFoundException se o ID informado para atualização não existir")
    void updateDeveLancarExcecaoQuandoIdNaoExiste() {
        // Arrange
        when(tipoUsuarioRepositoryPort.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> tipoUsuarioInteractor.update(1L, "ADMINISTRADOR"))
                .isInstanceOf(TipoUsuarioNotFoundException.class);

        verify(tipoUsuarioRepositoryPort, never()).save(any());
    }

    // ==========================================
    // TESTES: findById
    // ==========================================

    @Test
    @DisplayName("findById: Deve retornar o tipo de usuário quando encontrado no repositório")
    void findByIdDeveRetornarTipoUsuarioQuandoEncontrado() {
        // Arrange
        TipoUsuario tipoUsuarioEsperado = new TipoUsuario(1L, "GERENTE");
        when(tipoUsuarioRepositoryPort.findById(1L)).thenReturn(Optional.of(tipoUsuarioEsperado));

        // Act
        TipoUsuario resultado = tipoUsuarioInteractor.findById(1L);

        // Assert
        assertThat(resultado).isEqualTo(tipoUsuarioEsperado);
    }

    @Test
    @DisplayName("findById: Deve lançar TipoUsuarioNotFoundException quando o registro não existir")
    void findByIdDeveLancarExcecaoQuandoNaoEncontrado() {
        // Arrange
        when(tipoUsuarioRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> tipoUsuarioInteractor.findById(1L))
                .isInstanceOf(TipoUsuarioNotFoundException.class);
    }

    // ==========================================
    // TESTES: findAll
    // ==========================================

    @Test
    @DisplayName("findAll: Deve retornar a lista completa de todos os tipos de usuários")
    void findAllDeveRetornarListaCompleta() {
        // Arrange
        List<TipoUsuario> listaEsperada = List.of(
                new TipoUsuario(1L, "CLIENTE"),
                new TipoUsuario(2L, "GERENTE")
        );
        when(tipoUsuarioRepositoryPort.findAll()).thenReturn(listaEsperada);

        // Act
        List<TipoUsuario> resultado = tipoUsuarioInteractor.findAll();

        // Assert
        assertThat(resultado).hasSize(2).containsAll(listaEsperada);
    }

    // ==========================================
    // TESTES: existsById
    // ==========================================

    @Test
    @DisplayName("existsById: Deve retornar true se o Optional do findById contiver valor")
    void existsByIdDeveRetornarTrueQuandoRegistroExiste() {
        // Arrange
        TipoUsuario tipoUsuario = new TipoUsuario(1L, "CLIENTE");
        when(tipoUsuarioRepositoryPort.findById(1L)).thenReturn(Optional.of(tipoUsuario));

        // Act
        boolean resultado = tipoUsuarioInteractor.existsById(1L);

        // Assert
        assertThat(resultado).isTrue();
    }

    @Test
    @DisplayName("existsById: Deve retornar false se o Optional do findById estiver vazio")
    void existsByIdDeveRetornarFalseQuandoRegistroNaoExiste() {
        // Arrange
        when(tipoUsuarioRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        // Act
        boolean resultado = tipoUsuarioInteractor.existsById(1L);

        // Assert
        assertThat(resultado).isFalse();
    }

    // ==========================================
    // TESTES: delete
    // ==========================================

    @Test
    @DisplayName("delete: Deve deletar o tipo de usuário se o ID existir")
    void deleteDeveDeletarQuandoIdExiste() {
        // Arrange
        when(tipoUsuarioRepositoryPort.existsById(1L)).thenReturn(true);

        // Act
        tipoUsuarioInteractor.delete(1L);

        // Assert
        verify(tipoUsuarioRepositoryPort, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("delete: Deve lançar TipoUsuarioNotFoundException ao tentar deletar um ID inexistente")
    void deleteDeveLancarExcecaoQuandoIdNaoExiste() {
        // Arrange
        when(tipoUsuarioRepositoryPort.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> tipoUsuarioInteractor.delete(1L))
                .isInstanceOf(TipoUsuarioNotFoundException.class);

        verify(tipoUsuarioRepositoryPort, never()).deleteById(anyLong());
    }

}
