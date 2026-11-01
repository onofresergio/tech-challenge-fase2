package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.EmailAlreadyExistsException;
import br.com.fiap.techchallenge.domain.exception.UsuarioNotFoundException;
import br.com.fiap.techchallenge.domain.exception.UsuarioValidationException;
import br.com.fiap.techchallenge.domain.model.Usuario;
import br.com.fiap.techchallenge.usecase.port.output.PasswordHasherPort;
import br.com.fiap.techchallenge.usecase.port.output.UsuarioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioInteractorTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @Mock
    private PasswordHasherPort passwordHasherPort;

    @InjectMocks
    private UsuarioInteractor usuarioInteractor;

    private final LocalDateTime dataPadrao = LocalDateTime.now();

    // ==========================================
    // TESTES: findById
    // ==========================================

    @Test
    @DisplayName("findById: Deve retornar o usuário quando encontrado no repositório")
    void findByIdDeveRetornarUsuarioQuandoEncontrado() {
        // Arrange
        Usuario usuarioEsperado = new Usuario(1L, "John Doe", "john@email.com", "hash", "john", dataPadrao, 10L, 20L);
        when(usuarioRepositoryPort.findById(1L)).thenReturn(Optional.of(usuarioEsperado));

        // Act
        Usuario resultado = usuarioInteractor.findById(1L);

        // Assert
        assertThat(resultado).isEqualTo(usuarioEsperado);
    }

    @Test
    @DisplayName("findById: Deve lançar UsuarioNotFoundException quando o usuário não existir")
    void findByIdDeveLancarExcecaoQuandoNaoEncontrado() {
        // Arrange
        when(usuarioRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> usuarioInteractor.findById(1L))
                .isInstanceOf(UsuarioNotFoundException.class);
    }

    // ==========================================
    // TESTES: create
    // ==========================================

    @Test
    @DisplayName("create: Deve criar um novo usuário aplicando hash na senha e salvando com sucesso")
    void createDeveSalvarNovoUsuarioComSucesso() {
        // Arrange
        String senhaPura = "senha123";
        String senhaCripto = "senhaCriptografadaHash";
        Usuario usuarioSalvo = new Usuario(1L, "John Doe", "john@email.com", senhaCripto, "john", dataPadrao, 10L, 20L);

        when(usuarioRepositoryPort.findByEmail("john@email.com")).thenReturn(Optional.empty());
        when(passwordHasherPort.hash(senhaPura)).thenReturn(senhaCripto);
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // Act
        Usuario resultado = usuarioInteractor.create("John Doe", "john@email.com", senhaPura, "john", 10L, 20L);

        // Assert
        assertThat(resultado).isEqualTo(usuarioSalvo);

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepositoryPort).save(usuarioCaptor.capture());
        assertThat(usuarioCaptor.getValue().id()).isNull();
        assertThat(usuarioCaptor.getValue().senha()).isEqualTo(senhaCripto);
    }

    @Test
    @DisplayName("create: Deve lançar EmailAlreadyExistsException se o e-mail já estiver cadastrado")
    void createDeveLancarExcecaoQuandoEmailJaExiste() {
        // Arrange
        Usuario usuarioExistente = new Usuario(1L, "Outro", "john@email.com", "hash", "outro", dataPadrao, 10L, 20L);
        when(usuarioRepositoryPort.findByEmail("john@email.com")).thenReturn(Optional.of(usuarioExistente));

        // Act & Assert
        assertThatThrownBy(() -> usuarioInteractor.create("John Doe", "john@email.com", "senha", "john", 10L, 20L))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(usuarioRepositoryPort, never()).save(any());
    }

    // ==========================================
    // TESTES: update
    // ==========================================

    @Test
    @DisplayName("update: Deve atualizar dados mantendo a senha antiga e validando quando o e-mail é idêntico ao atual")
    void updateDeveAtualizarComSucessoMantendoMesmoEmail() {
        // Arrange
        Usuario usuarioAtual = new Usuario(1L, "John", "john@email.com", "senhaAntigaHash", "john", dataPadrao, 10L, 20L);
        Usuario usuarioSalvo = new Usuario(1L, "John Alterado", "john@email.com", "senhaAntigaHash", "john_new", dataPadrao, 11L, 21L);

        when(usuarioRepositoryPort.findById(1L)).thenReturn(Optional.of(usuarioAtual));
        when(usuarioRepositoryPort.findByEmail("john@email.com")).thenReturn(Optional.of(usuarioAtual)); // Mesmo ID, deve permitir
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // Act
        Usuario resultado = usuarioInteractor.update(1L, "John Alterado", "john@email.com", "john_new", 11L, 21L);

        // Assert
        assertThat(resultado).isEqualTo(usuarioSalvo);
    }

    @Test
    @DisplayName("update: Deve lançar EmailAlreadyExistsException ao atualizar se o e-mail pertencer a outro usuário cadastrado")
    void updateDeveLancarExcecaoQuandoEmailPertenceAOutroUsuario() {
        // Arrange
        Usuario usuarioAtual = new Usuario(1L, "John", "john@email.com", "hash", "john", dataPadrao, 10L, 20L);
        Usuario usuarioConcorrente = new Usuario(2L, "Maria", "maria@email.com", "hash", "maria", dataPadrao, 10L, 20L);

        when(usuarioRepositoryPort.findById(1L)).thenReturn(Optional.of(usuarioAtual));
        when(usuarioRepositoryPort.findByEmail("maria@email.com")).thenReturn(Optional.of(usuarioConcorrente)); // ID diferente detectado

        // Act & Assert
        assertThatThrownBy(() -> usuarioInteractor.update(1L, "John", "maria@email.com", "john", 10L, 20L))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("update: Deve lançar UsuarioNotFoundException se o usuário a ser atualizado não existir")
    void updateDeveLancarExcecaoSeUsuarioNaoExiste() {
        // Arrange
        when(usuarioRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> usuarioInteractor.update(1L, "John", "john@email.com", "john", 10L, 20L))
                .isInstanceOf(UsuarioNotFoundException.class);
    }

    // ==========================================
    // TESTES: changePassword
    // ==========================================

    @Test
    @DisplayName("changePassword: Deve alterar a senha aplicando hash com sucesso")
    void changePasswordDeveAlterarSenhaComSucesso() {
        // Arrange
        Usuario usuarioAtual = new Usuario(1L, "John", "john@email.com", "hashAntigo", "john", dataPadrao, 10L, 20L);
        Usuario usuarioSalvo = new Usuario(1L, "John", "john@email.com", "hashNovo", "john", dataPadrao, 10L, 20L);

        when(usuarioRepositoryPort.findById(1L)).thenReturn(Optional.of(usuarioAtual));
        when(passwordHasherPort.hash("novaSenha")).thenReturn("hashNovo");
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // Act
        Usuario resultado = usuarioInteractor.changePassword(1L, "novaSenha");

        // Assert
        assertThat(resultado.senha()).isEqualTo("hashNovo");
        verify(usuarioRepositoryPort).save(any(Usuario.class));
    }

    @Test
    @DisplayName("changePassword: Deve lançar UsuarioNotFoundException se o usuário não for encontrado")
    void changePasswordDeveLancarExcecaoSeUsuarioNaoExiste() {
        // Arrange
        when(usuarioRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> usuarioInteractor.changePassword(1L, "novaSenha"))
                .isInstanceOf(UsuarioNotFoundException.class);
    }

    // ==========================================
    // TESTES: delete
    // ==========================================

    @Test
    @DisplayName("delete: Deve deletar o usuário caso o ID exista no repositório")
    void deleteDeveDeletarSeUsuarioExistir() {
        // Arrange
        when(usuarioRepositoryPort.existsById(1L)).thenReturn(true);

        // Act
        usuarioInteractor.delete(1L);

        // Assert
        verify(usuarioRepositoryPort, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("delete: Deve lançar UsuarioNotFoundException ao tentar deletar se o ID não existir")
    void deleteDeveLancarExcecaoSeIdNaoExiste() {
        // Arrange
        when(usuarioRepositoryPort.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> usuarioInteractor.delete(1L))
                .isInstanceOf(UsuarioNotFoundException.class);

        verify(usuarioRepositoryPort, never()).deleteById(anyLong());
    }

    // ==========================================
    // TESTES: findAll e existsById
    // ==========================================

    @Test
    @DisplayName("findAll: Deve retornar a lista completa de usuários cadastrados")
    void findAllDeveRetornarLista() {
        // Arrange
        List<Usuario> lista = List.of(new Usuario(1L, "John", "john@email.com", "hash", "john", dataPadrao, 10L, 20L));
        when(usuarioRepositoryPort.findAll()).thenReturn(lista);

        // Act
        List resultado = usuarioInteractor.findAll();

        // Assert
        assertThat(resultado).hasSize(1).containsAll(lista);
    }

    @Test
    @DisplayName("existsById: Deve retornar booleano conforme o resultado do repositório")
    void existsByIdDeveRepassarResultadoDoPort() {
        // Arrange & Act & Assert
        when(usuarioRepositoryPort.existsById(1L)).thenReturn(true);
        assertThat(usuarioInteractor.existsById(1L)).isTrue();
        when(usuarioRepositoryPort.existsById(2L)).thenReturn(false);
        assertThat(usuarioInteractor.existsById(2L)).isFalse();
    }

    // ==========================================
    // TESTES: findByNomeContaining
    // ==========================================

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("findByNomeContaining: Deve lançar UsuarioValidationException se termo de busca for vazio ou nulo")
    void findByNomeContainingDeveLancarExcecaoSeNomeInvalido(String nomeInvalido) {
        // Act & Assert
        assertThatThrownBy(() -> usuarioInteractor.findByNomeContaining(nomeInvalido))
                .isInstanceOf(UsuarioValidationException.class)
                .hasMessage("O nome para busca não pode ser vazio.");
    }

//    @Test
//    @DisplayName("findByNomeContaining: Deve filtrar a lista ignorando maiúsculas/minúsculas e trazendo apenas registros correspondentes")
//    void findByNomeContainingDeveFiltrarCorretamente() {
//        // Arrange - Todos os usuários agora possuem nomes válidos que respeitam as regras do Record
//        Usuario u1 = new Usuario(1L, "Guilherme Santos", "g@email.com", "hash", "g1", dataPadrao, 10L, 20L);
//        Usuario u2 = new Usuario(2L, "Marta SILVA", "m@email.com", "hash", "m2", dataPadrao, 10L, 20L);
//        Usuario u3 = new Usuario(3L, "Carlos Andrade", "carlos@email.com", "hash", "c3", dataPadrao, 10L, 20L);
//        Usuario u4 = new Usuario(4L, "Felipe Lima", "f@email.com", "hash", "f4", dataPadrao, 10L, 20L);
//
//        when(usuarioRepositoryPort.findAll()).thenReturn(List.of(u1, u2, u3, u4));
//
//        // Act
//        List<Usuario> filtrados = usuarioInteractor.findByNomeContaining("silva");
//
//        // Assert
//        assertThat(filtrados)
//                .hasSize(1)
//                .extracting(Usuario::nome)
//                .containsExactly("Marta SILVA");
//    }

    @Test
    @DisplayName("findByNomeContaining: Deve filtrar a lista e testar todas as ramificações lógicas com sucesso")
    void findByNomeContainingDeveFiltrarCorretamente() {
        // Arrange
        Usuario u1 = new Usuario(1L, "Guilherme Santos", "g@email.com", "hash", "g1", dataPadrao, 10L, 20L);
        Usuario u2 = new Usuario(2L, "Marta SILVA", "m@email.com", "hash", "m2", dataPadrao, 10L, 20L);
        Usuario u4 = new Usuario(4L, "Felipe Lima", "f@email.com", "hash", "f4", dataPadrao, 10L, 20L);

        // Criamos um MOCK do Record para forçar o nome a ser nulo e cobrir a primeira branch do filter
        Usuario uMockComNomeNulo = mock(Usuario.class);
        when(uMockComNomeNulo.nome()).thenReturn(null);

        // Injetamos o mock no meio da lista retornada pelo port
        when(usuarioRepositoryPort.findAll()).thenReturn(List.of(u1, u2, uMockComNomeNulo, u4));

        // Act
        List<Usuario> filtrados = usuarioInteractor.findByNomeContaining("silva");

        // Assert
        assertThat(filtrados)
                .hasSize(1)
                .extracting(Usuario::nome)
                .containsExactly("Marta SILVA");
    }
}
