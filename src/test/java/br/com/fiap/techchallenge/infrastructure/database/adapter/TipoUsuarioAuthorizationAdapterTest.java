package br.com.fiap.techchallenge.infrastructure.database.adapter;

import br.com.fiap.techchallenge.domain.model.Usuario;
import br.com.fiap.techchallenge.infrastructure.database.entity.UsuarioEntity;
import br.com.fiap.techchallenge.infrastructure.database.jpa.TipoUsuarioJpaRepository;
import br.com.fiap.techchallenge.infrastructure.database.jpa.UsuarioJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TipoUsuarioAuthorizationAdapterTest {

    @Mock
    private UsuarioJpaRepository usuarioRepository;

    @Mock
    private TipoUsuarioJpaRepository tipoUsuarioRepository;

    @InjectMocks
    private TipoUsuarioAuthorizationAdapter tipoUsuarioAuthorizationAdapter;

    @ParameterizedTest
    @ValueSource(strings = {"DONO DE RESTAURANTE", "dono de restaurante", "Dono de Restaurante"})
    @DisplayName("isRestaurantOwner: Deve retornar true se o usuário tiver tipo de usuário proprietário (independente de Case)")
    void isRestaurantOwnerDeveRetornarTrueQuandoForDono(String nomeTipo) {
        Long usuarioId = 10L;
        Long tipoUsuarioId = 1L;

        Usuario usuario = new Usuario(10L, "Nome", "teste@email.com", "senha", "login", LocalDateTime.now(), 1L, tipoUsuarioId);

        var tipoEntity = mock(br.com.fiap.techchallenge.infrastructure.database.entity.TipoUsuarioEntity.class);
        when(tipoEntity.getNome()).thenReturn(nomeTipo);
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(UsuarioEntity.fromDomain(usuario)));
        when(tipoUsuarioRepository.findById(tipoUsuarioId)).thenReturn(Optional.of(tipoEntity));

        boolean resultado = tipoUsuarioAuthorizationAdapter.isRestaurantOwner(usuarioId);

        assertThat(resultado).isTrue();
        verify(usuarioRepository, times(1)).findById(usuarioId);
        verify(tipoUsuarioRepository, times(1)).findById(tipoUsuarioId);
    }

    @Test
    @DisplayName("isRestaurantOwner: Deve retornar false se o usuário não for dono de restaurante")
    void isRestaurantOwnerDeveRetornarFalseQuandoForOutroTipo() {
        Long usuarioId = 11L;
        Long tipoUsuarioId = 2L;

        Usuario usuario = new Usuario(11L, "Nome", "teste@email.com", "senha", "login", LocalDateTime.now(), 1L, tipoUsuarioId);

        var tipoEntity = mock(br.com.fiap.techchallenge.infrastructure.database.entity.TipoUsuarioEntity.class);
        when(tipoEntity.getNome()).thenReturn("CLIENTE");
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(UsuarioEntity.fromDomain(usuario)));
        when(tipoUsuarioRepository.findById(tipoUsuarioId)).thenReturn(Optional.of(tipoEntity));

        boolean resultado = tipoUsuarioAuthorizationAdapter.isRestaurantOwner(usuarioId);

        assertThat(resultado).isFalse();
        verify(usuarioRepository, times(1)).findById(usuarioId);
        verify(tipoUsuarioRepository, times(1)).findById(tipoUsuarioId);
    }

    @Test
    @DisplayName("isRestaurantOwner: Deve retornar false se o usuário não for encontrado no banco")
    void isRestaurantOwnerDeveRetornarFalseQuandoIdNaoExistir() {
        Long usuarioId = 12L;
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        boolean resultado = tipoUsuarioAuthorizationAdapter.isRestaurantOwner(usuarioId);

        assertThat(resultado).isFalse();
        verify(usuarioRepository, times(1)).findById(usuarioId);
        verifyNoInteractions(tipoUsuarioRepository);
    }
}
