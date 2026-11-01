package br.com.fiap.techchallenge.infrastructure.database.adapter;

import br.com.fiap.techchallenge.infrastructure.database.jpa.TipoUsuarioJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TipoUsuarioAuthorizationAdapterTest {

    @Mock
    private TipoUsuarioJpaRepository repository;

    @InjectMocks
    private TipoUsuarioAuthorizationAdapter tipoUsuarioAuthorizationAdapter;

    @ParameterizedTest
    @ValueSource(strings = {"DONO DE RESTAURANTE", "dono de restaurante", "Dono de Restaurante"})
    @DisplayName("isRestaurantOwner: Deve retornar true se o tipo de usuário for proprietário (independente de Case)")
    void isRestaurantOwnerDeveRetornarTrueQuandoForDono(String nomeTipo) {
        // Arrange
        Long tipoUsuarioId = 1L;

        // Mock genérico da entidade retornada pelo TipoUsuarioJpaRepository
        var entityMock = mock(br.com.fiap.techchallenge.infrastructure.database.entity.TipoUsuarioEntity.class);
        when(entityMock.getNome()).thenReturn(nomeTipo);
        when(repository.findById(tipoUsuarioId)).thenReturn(Optional.of(entityMock));

        // Act
        boolean resultado = tipoUsuarioAuthorizationAdapter.isRestaurantOwner(tipoUsuarioId);

        // Assert
        assertThat(resultado).isTrue();
        verify(repository, times(1)).findById(tipoUsuarioId);
    }

    @Test
    @DisplayName("isRestaurantOwner: Deve retornar false se o tipo de usuário existir mas não for o dono")
    void isRestaurantOwnerDeveRetornarFalseQuandoForOutroTipo() {
        // Arrange
        Long tipoUsuarioId = 2L;

        var entityMock = mock(br.com.fiap.techchallenge.infrastructure.database.entity.TipoUsuarioEntity.class);
        when(entityMock.getNome()).thenReturn("CLIENTE");
        when(repository.findById(tipoUsuarioId)).thenReturn(Optional.of(entityMock));

        // Act
        boolean resultado = tipoUsuarioAuthorizationAdapter.isRestaurantOwner(tipoUsuarioId);

        // Assert
        assertThat(resultado).isFalse();
        verify(repository, times(1)).findById(tipoUsuarioId);
    }

    @Test
    @DisplayName("isRestaurantOwner: Deve retornar false se o tipo de usuário não for encontrado no banco")
    void isRestaurantOwnerDeveRetornarFalseQuandoIdNaoExistir() {
        // Arrange
        Long tipoUsuarioId = 3L;
        when(repository.findById(tipoUsuarioId)).thenReturn(Optional.empty()); // Ativa o .orElse(false)

        // Act
        boolean resultado = tipoUsuarioAuthorizationAdapter.isRestaurantOwner(tipoUsuarioId);

        // Assert
        assertThat(resultado).isFalse();
        verify(repository, times(1)).findById(tipoUsuarioId);
    }
}
