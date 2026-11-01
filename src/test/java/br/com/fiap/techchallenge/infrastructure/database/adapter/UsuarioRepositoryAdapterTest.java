package br.com.fiap.techchallenge.infrastructure.database.adapter;

import br.com.fiap.techchallenge.domain.model.Usuario;
import br.com.fiap.techchallenge.infrastructure.database.entity.UsuarioEntity;
import br.com.fiap.techchallenge.infrastructure.database.jpa.UsuarioJpaRepository;
import br.com.fiap.techchallenge.usecase.port.output.PasswordHasherPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioRepositoryAdapterTest {

    @Mock
    private UsuarioJpaRepository repository;

    @Mock
    private PasswordHasherPort passwordHasherPort;

    @InjectMocks
    private UsuarioRepositoryAdapter usuarioRepositoryAdapter;

    private final LocalDateTime dataPadrao = LocalDateTime.of(2026, 10, 12, 15, 0);
    private final Usuario usuarioDomain = new Usuario(1L, "John Doe", "john@email.com", "hash", "john", dataPadrao, 10L, 20L);

    // ==========================================
    // TESTES: Construtores
    // ==========================================

    @Test
    @DisplayName("Constructor: Deve instanciar a classe com sucesso através do construtor legado/depreciado")
    void deveInstanciarComConstrutorDepreciado() {
        // Act
        UsuarioRepositoryAdapter adapterLegado = new UsuarioRepositoryAdapter(repository, passwordHasherPort);

        // Assert
        assertThat(adapterLegado).isNotNull();
    }

    // ==========================================
    // TESTES: save
    // ==========================================

    @Test
    @DisplayName("save: Deve converter o modelo de domínio para entidade, salvar no JPA e retornar o domínio correspondente")
    void saveDevePersistirERetornarModeloDeDominio() {
        // Arrange
        UsuarioEntity entityMock = mock(UsuarioEntity.class);
        when(entityMock.toDomain()).thenReturn(usuarioDomain);
        when(repository.save(any(UsuarioEntity.class))).thenReturn(entityMock);

        // Act
        Usuario resultado = usuarioRepositoryAdapter.save(usuarioDomain);

        // Assert
        assertThat(resultado).isNotNull().isEqualTo(usuarioDomain);
        verify(repository, times(1)).save(any(UsuarioEntity.class));
    }

    // ==========================================
    // TESTES: findById
    // ==========================================

    @Test
    @DisplayName("findById: Deve retornar um Optional contendo o domínio quando o registro existir no banco")
    void findByIdDeveRetornarOptionalComDominioQuandoExistir() {
        // Arrange
        UsuarioEntity entityMock = mock(UsuarioEntity.class);
        when(entityMock.toDomain()).thenReturn(usuarioDomain);
        when(repository.findById(1L)).thenReturn(Optional.of(entityMock));

        // Act
        Optional<Usuario> resultado = usuarioRepositoryAdapter.findById(1L);

        // Assert
        assertThat(resultado).isPresent().contains(usuarioDomain);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById: Deve retornar um Optional vazio quando o ID não for localizado no repositório JPA")
    void findByIdDeveRetornarOptionalVazioQuandoNaoExistir() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Usuario> resultado = usuarioRepositoryAdapter.findById(1L);

        // Assert
        assertThat(resultado).isEmpty();
    }

    // ==========================================
    // TESTES: findByEmail
    // ==========================================

    @Test
    @DisplayName("findByEmail: Deve retornar um Optional contendo o domínio quando o e-mail existir no banco")
    void findByEmailDeveRetornarOptionalComDominioQuandoExistir() {
        // Arrange
        UsuarioEntity entityMock = mock(UsuarioEntity.class);
        when(entityMock.toDomain()).thenReturn(usuarioDomain);
        when(repository.findByEmail("john@email.com")).thenReturn(Optional.of(entityMock));

        // Act
        Optional<Usuario> resultado = usuarioRepositoryAdapter.findByEmail("john@email.com");

        // Assert
        assertThat(resultado).isPresent().contains(usuarioDomain);
        verify(repository, times(1)).findByEmail("john@email.com");
    }

    @Test
    @DisplayName("findByEmail: Deve retornar um Optional vazio quando o e-mail não for localizado no repositório JPA")
    void findByEmailDeveRetornarOptionalVazioQuandoNaoExistir() {
        // Arrange
        when(repository.findByEmail("john@email.com")).thenReturn(Optional.empty());

        // Act
        Optional<Usuario> resultado = usuarioRepositoryAdapter.findByEmail("john@email.com");

        // Assert
        assertThat(resultado).isEmpty();
    }

    // ==========================================
    // TESTES: findAll
    // ==========================================

    @Test
    @DisplayName("findAll: Deve retornar uma lista mapeada para o domínio quando houver registros no banco")
    void findAllDeveRetornarListaDeDominioPopulada() {
        // Arrange
        UsuarioEntity entityMock = mock(UsuarioEntity.class);
        when(entityMock.toDomain()).thenReturn(usuarioDomain);
        when(repository.findAll()).thenReturn(List.of(entityMock));

        // Act
        List<Usuario> resultado = usuarioRepositoryAdapter.findAll();

        // Assert
        assertThat(resultado).isNotEmpty().hasSize(1).containsExactly(usuarioDomain);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll: Deve retornar uma lista vazia se o repositório JPA não possuir registros")
    void findAllDeveRetornarListaVaziaQuandoNaoHouverDados() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Usuario> resultado = usuarioRepositoryAdapter.findAll();

        // Assert
        assertThat(resultado).isEmpty();
    }

    // ==========================================
    // TESTES: deleteById e existsById
    // ==========================================

    @Test
    @DisplayName("deleteById: Deve invocar a deleção no repositório JPA corretamente")
    void deleteByIdDeveInvocarRemocaoNoJpa() {
        // Act
        usuarioRepositoryAdapter.deleteById(1L);

        // Assert
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("existsById: Deve repassar de forma transparente o retorno booleano do repositório JPA")
    void existsByIdDeveRetornarBooleanoDoJpa() {
        // Arrange
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.existsById(2L)).thenReturn(false);

        // Act & Assert
        assertThat(usuarioRepositoryAdapter.existsById(1L)).isTrue();
        assertThat(usuarioRepositoryAdapter.existsById(2L)).isFalse();

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).existsById(2L);
    }
}
