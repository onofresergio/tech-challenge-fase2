package br.com.fiap.techchallenge.usecase.assembler;

import br.com.fiap.techchallenge.domain.model.Endereco;
import br.com.fiap.techchallenge.domain.model.TipoUsuario;
import br.com.fiap.techchallenge.domain.model.Usuario;
import br.com.fiap.techchallenge.infrastructure.controller.response.UsuarioResponse;
import br.com.fiap.techchallenge.usecase.port.output.EnderecoRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioResponseAssemblerTest {

    @Mock
    private EnderecoRepositoryPort enderecoRepositoryPort;

    @Mock
    private TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort;

    @InjectMocks
    private UsuarioResponseAssembler usuarioResponseAssembler;

    private final LocalDateTime dataAlteracao = LocalDateTime.of(2026, 10, 12, 14, 0);

    @Test
    @DisplayName("Deve converter para UsuarioResponse com todos os relacionamentos preenchidos com sucesso")
    void deveConverterParaUsuarioResponseCompleto() {
        // Arrange
        Usuario usuario = new Usuario(1L, "John Doe", "john@example.com", "senha123", "johndoe", dataAlteracao, 10L, 20L);
        Endereco endereco = new Endereco(10L, "Av. Paulista", "1000", "São Paulo", "SP", "01311-100");
        TipoUsuario tipoUsuario = new TipoUsuario(20L, "CLIENTE");

        when(enderecoRepositoryPort.findById(10L)).thenReturn(Optional.of(endereco));
        when(tipoUsuarioRepositoryPort.findById(20L)).thenReturn(Optional.of(tipoUsuario));

        // Act
        UsuarioResponse response = usuarioResponseAssembler.toResponse(usuario);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("John Doe");
        assertThat(response.email()).isEqualTo("john@example.com");
        assertThat(response.login()).isEqualTo("johndoe");

        // Verifica mapeamento do EnderecoResponse
        assertThat(response.endereco()).isNotNull();
        assertThat(response.endereco().id()).isEqualTo(10L);
        assertThat(response.endereco().rua()).isEqualTo("Av. Paulista");

        // Verifica mapeamento do TipoUsuarioResponse
        assertThat(response.tipoUsuario()).isNotNull();
        assertThat(response.tipoUsuario().id()).isEqualTo(20L);
        assertThat(response.tipoUsuario().nome()).isEqualTo("CLIENTE");
    }

    @Test
    @DisplayName("Deve converter para UsuarioResponse com sub-relacionamentos nulos quando as portas retornarem vazio")
    void deveConverterParaUsuarioResponseComRelacionamentosNulos() {
        // Arrange
        Usuario usuario = new Usuario(1L, "John Doe", "john@example.com", "senha123", "johndoe", dataAlteracao, 10L, 20L);

        // Simula o comportamento das portas retornando Optional.empty() para cobrir as cláusulas .orElse(null)
        when(enderecoRepositoryPort.findById(10L)).thenReturn(Optional.empty());
        when(tipoUsuarioRepositoryPort.findById(20L)).thenReturn(Optional.empty());

        // Act
        UsuarioResponse response = usuarioResponseAssembler.toResponse(usuario);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("John Doe");
        assertThat(response.endereco()).isNull();
        assertThat(response.tipoUsuario()).isNull();
    }

}
