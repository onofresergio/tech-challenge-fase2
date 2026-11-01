package br.com.fiap.techchallenge.infrastructure.database.adapter;

import br.com.fiap.techchallenge.domain.model.Endereco;
import br.com.fiap.techchallenge.infrastructure.database.entity.EnderecoEntity;
import br.com.fiap.techchallenge.infrastructure.database.jpa.EnderecoJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnderecoRepositoryAdapterTest {

    @Mock
    private EnderecoJpaRepository repository;

    @InjectMocks
    private EnderecoRepositoryAdapter enderecoRepositoryAdapter;

    private final Endereco enderecoDomain = new Endereco(1L, "Av. Paulista", "1000", "São Paulo", "SP", "01311-100");

    // ==========================================
    // TESTES: save
    // ==========================================

    @Test
    @DisplayName("save: Deve converter o modelo de domínio para entidade, salvar no JPA e retornar o domínio correspondente")
    void saveDevePersistirERetornarModeloDeDominio() {
        // Arrange
        EnderecoEntity entityMock = mock(EnderecoEntity.class);
        when(entityMock.toDomain()).thenReturn(enderecoDomain);
        when(repository.save(any(EnderecoEntity.class))).thenReturn(entityMock);

        // Act
        Endereco resultado = enderecoRepositoryAdapter.save(enderecoDomain);

        // Assert
        assertThat(resultado).isNotNull().isEqualTo(enderecoDomain);
        verify(repository, times(1)).save(any(EnderecoEntity.class));
    }

    // ==========================================
    // TESTES: findById
    // ==========================================

    @Test
    @DisplayName("findById: Deve retornar um Optional contendo o domínio quando o registro existir no banco de dados")
    void findByIdDeveRetornarOptionalComDominioQuandoExistir() {
        // Arrange
        EnderecoEntity entityMock = mock(EnderecoEntity.class);
        when(entityMock.toDomain()).thenReturn(enderecoDomain);
        when(repository.findById(1L)).thenReturn(Optional.of(entityMock));

        // Act
        Optional<Endereco> resultado = enderecoRepositoryAdapter.findById(1L);

        // Assert
        assertThat(resultado).isPresent().contains(enderecoDomain);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById: Deve retornar un Optional vazio quando o ID não for localizado no repositório JPA")
    void findByIdDeveRetornarOptionalVazioQuandoNaoExistir() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Endereco> resultado = enderecoRepositoryAdapter.findById(1L);

        // Assert
        assertThat(resultado).isEmpty();
        verify(repository, times(1)).findById(1L);
    }

    // ==========================================
    // TESTES: findAll
    // ==========================================

    @Test
    @DisplayName("findAll: Deve retornar uma lista mapeada para o domínio quando houver registros no banco")
    void findAllDeveRetornarListaDeDominioPopulada() {
        // Arrange
        EnderecoEntity entityMock = mock(EnderecoEntity.class);
        when(entityMock.toDomain()).thenReturn(enderecoDomain);
        when(repository.findAll()).thenReturn(List.of(entityMock));

        // Act
        List<Endereco> resultado = enderecoRepositoryAdapter.findAll();

        // Assert
        assertThat(resultado).isNotEmpty().hasSize(1).containsExactly(enderecoDomain);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll: Deve retornar uma lista vazia se o repositório JPA não possuir registros")
    void findAllDeveRetornarListaVaziaQuandoNaoHouverDados() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Endereco> resultado = enderecoRepositoryAdapter.findAll();

        // Assert
        assertThat(resultado).isEmpty();
        verify(repository, times(1)).findAll();
    }

    // ==========================================
    // TESTES: deleteById e existsById
    // ==========================================

    @Test
    @DisplayName("deleteById: Deve invocar a deleção no repositório JPA corretamente")
    void deleteByIdDeveInvocarRemocaoNoJpa() {
        // Act
        enderecoRepositoryAdapter.deleteById(1L);

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
        assertThat(enderecoRepositoryAdapter.existsById(1L)).isTrue();
        assertThat(enderecoRepositoryAdapter.existsById(2L)).isFalse();

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).existsById(2L);
    }
}
