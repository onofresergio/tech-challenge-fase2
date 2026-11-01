package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.EnderecoNotFoundException;
import br.com.fiap.techchallenge.domain.model.Endereco;
import br.com.fiap.techchallenge.usecase.port.output.EnderecoRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
public class EnderecoInteractorTest {

    @Mock
    private EnderecoRepositoryPort enderecoRepositoryPort;

    @InjectMocks
    private EnderecoInteractor enderecoInteractor;

    // ==========================================
    // TESTES: findById
    // ==========================================

    @Test
    @DisplayName("findById: Deve retornar o endereço quando encontrado no repositório")
    void findByIdDeveRetornarEnderecoQuandoEncontrado() {
        // Arrange
        Endereco enderecoEsperado = new Endereco(1L, "Av. Paulista", "1000", "São Paulo", "SP", "01311-100");
        when(enderecoRepositoryPort.findById(1L)).thenReturn(Optional.of(enderecoEsperado));

        // Act
        Endereco resultado = enderecoInteractor.findById(1L);

        // Assert
        assertThat(resultado).isEqualTo(enderecoEsperado);
    }

    @Test
    @DisplayName("findById: Deve lançar EnderecoNotFoundException quando o endereço não existir")
    void findByIdDeveLancarExcecaoQuandoNaoEncontrado() {
        // Arrange
        when(enderecoRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> enderecoInteractor.findById(1L))
                .isInstanceOf(EnderecoNotFoundException.class);
    }

    // ==========================================
    // TESTES: create
    // ==========================================

    @Test
    @DisplayName("create: Deve instanciar e salvar um novo endereço com sucesso")
    void createDeveSalvarNovoEnderecoComSucesso() {
        // Arrange
        Endereco enderecoSalvo = new Endereco(1L, "Av. Paulista", "1000", "São Paulo", "SP", "01311-100");
        when(enderecoRepositoryPort.save(any(Endereco.class))).thenReturn(enderecoSalvo);

        // Act
        Endereco resultado = enderecoInteractor.create("Av. Paulista", "1000", "São Paulo", "SP", "01311-100");

        // Assert
        assertThat(resultado).isEqualTo(enderecoSalvo);

        // Captura o argumento enviado ao repositório para validar as invariantes do Record
        ArgumentCaptor<Endereco> enderecoCaptor = ArgumentCaptor.forClass(Endereco.class);
        verify(enderecoRepositoryPort).save(enderecoCaptor.capture());
        assertThat(enderecoCaptor.getValue().id()).isNull(); // ID precisa ser nulo na criação
        assertThat(enderecoCaptor.getValue().rua()).isEqualTo("Av. Paulista");
    }

    // ==========================================
    // TESTES: update
    // ==========================================

    @Test
    @DisplayName("update: Deve atualizar e salvar o endereço com sucesso se o ID existir")
    void updateDeveSalvarComSucessoQuandoIdExiste() {
        // Arrange
        Endereco enderecoSalvo = new Endereco(1L, "Rua Augusta", "500", "São Paulo", "SP", "01305-000");
        when(enderecoRepositoryPort.existsById(1L)).thenReturn(true);
        when(enderecoRepositoryPort.save(any(Endereco.class))).thenReturn(enderecoSalvo);

        // Act
        Endereco resultado = enderecoInteractor.update(1L, "Rua Augusta", "500", "São Paulo", "SP", "01305-000");

        // Assert
        assertThat(resultado).isEqualTo(enderecoSalvo);

        ArgumentCaptor<Endereco> enderecoCaptor = ArgumentCaptor.forClass(Endereco.class);
        verify(enderecoRepositoryPort).save(enderecoCaptor.capture());
        assertThat(enderecoCaptor.getValue().id()).isEqualTo(1L); // ID deve ser mantido no update
    }

    @Test
    @DisplayName("update: Deve lançar EnderecoNotFoundException se o ID do endereço a ser atualizado não existir")
    void updateDeveLancarExcecaoQuandoIdNaoExiste() {
        // Arrange
        when(enderecoRepositoryPort.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> enderecoInteractor.update(1L, "Rua Augusta", "500", "São Paulo", "SP", "01305-000"))
                .isInstanceOf(EnderecoNotFoundException.class);

        verify(enderecoRepositoryPort, never()).save(any());
    }

    // ==========================================
    // TESTES: delete
    // ==========================================

    @Test
    @DisplayName("delete: Deve remover o endereço do repositório caso o ID exista")
    void deleteDeveDeletarQuandoIdExiste() {
        // Arrange
        when(enderecoRepositoryPort.existsById(1L)).thenReturn(true);

        // Act
        enderecoInteractor.delete(1L);

        // Assert
        verify(enderecoRepositoryPort, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("delete: Deve lançar EnderecoNotFoundException ao tentar deletar um ID inexistente")
    void deleteDeveLancarExcecaoQuandoIdNaoExiste() {
        // Arrange
        when(enderecoRepositoryPort.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> enderecoInteractor.delete(1L))
                .isInstanceOf(EnderecoNotFoundException.class);

        verify(enderecoRepositoryPort, never()).deleteById(anyLong());
    }

    // ==========================================
    // TESTES: findAll e existsById
    // ==========================================

    @Test
    @DisplayName("findAll: Deve retornar a lista de todos os endereços cadastrados")
    void findAllDeveRetornarListaCompleta() {
        // Arrange
        List<Endereco> listaEsperada = List.of(
                new Endereco(1L, "Av. Paulista", "1000", "São Paulo", "SP", "01311-100")
        );
        when(enderecoRepositoryPort.findAll()).thenReturn(listaEsperada);

        // Act
        List<Endereco> resultado = enderecoInteractor.findAll();

        // Assert
        assertThat(resultado).hasSize(1).containsAll(listaEsperada);
    }

    @Test
    @DisplayName("existsById: Deve repassar exatamente o resultado booleano da porta de saída")
    void existsByIdDeveRetornarBooleanoConformeOId() {
        // Arrange & Act & Assert
        when(enderecoRepositoryPort.existsById(1L)).thenReturn(true);
        assertThat(enderecoInteractor.existsById(1L)).isTrue();

        when(enderecoRepositoryPort.existsById(2L)).thenReturn(false);
        assertThat(enderecoInteractor.existsById(2L)).isFalse();
    }

}
