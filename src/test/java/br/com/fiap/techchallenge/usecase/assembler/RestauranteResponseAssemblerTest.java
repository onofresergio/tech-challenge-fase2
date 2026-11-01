package br.com.fiap.techchallenge.usecase.assembler;

import br.com.fiap.techchallenge.domain.model.Endereco;
import br.com.fiap.techchallenge.domain.model.Restaurante;
import br.com.fiap.techchallenge.domain.model.TipoCozinha;
import br.com.fiap.techchallenge.domain.model.TipoUsuario;
import br.com.fiap.techchallenge.infrastructure.controller.response.RestauranteResponse;
import br.com.fiap.techchallenge.usecase.port.output.EnderecoRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.TipoCozinhaRepositoryPort;
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
public class RestauranteResponseAssemblerTest {

    @Mock
    private EnderecoRepositoryPort enderecoRepositoryPort;

    @Mock
    private TipoCozinhaRepositoryPort tipoCozinhaRepositoryPort;

    @Mock
    private TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort;

    @InjectMocks
    private RestauranteResponseAssembler restauranteResponseAssembler;

    private final LocalDateTime horarioFuncionamento = LocalDateTime.of(2026, 10, 12, 12, 0);

    @Test
    @DisplayName("Deve converter para RestauranteResponse com todos os sub-relacionamentos preenchidos")
    void deveConverterParaRestauranteResponseCompleto() {
        // Arrange
        Restaurante restaurante = new Restaurante(1L, "Sabor das Américas", 10L, 20L, horarioFuncionamento, 30L);
        Endereco endereco = new Endereco(10L, "Av. Paulista", "1000", "São Paulo", "SP", "01311-100");
        TipoCozinha tipoCozinha = new TipoCozinha(20L, "Brasileira");
        TipoUsuario tipoUsuario = new TipoUsuario(30L, "PROPRIETARIO");

        when(enderecoRepositoryPort.findById(10L)).thenReturn(Optional.of(endereco));
        when(tipoCozinhaRepositoryPort.findById(20L)).thenReturn(Optional.of(tipoCozinha));
        when(tipoUsuarioRepositoryPort.findById(30L)).thenReturn(Optional.of(tipoUsuario));

        // Act
        RestauranteResponse response = restauranteResponseAssembler.toResponse(restaurante);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Sabor das Américas");
        assertThat(response.horarioFuncionamento()).isEqualTo(horarioFuncionamento);

        // Validação das sub-respostas mapeadas via .map()
        assertThat(response.endereco()).isNotNull();
        assertThat(response.endereco().id()).isEqualTo(10L);
        assertThat(response.endereco().rua()).isEqualTo("Av. Paulista");

        assertThat(response.tipoCozinha()).isNotNull();
        assertThat(response.tipoCozinha().id()).isEqualTo(20L);
        assertThat(response.tipoCozinha().nome()).isEqualTo("Brasileira");

        assertThat(response.tipoUsuario()).isNotNull();
        assertThat(response.tipoUsuario().id()).isEqualTo(30L);
        assertThat(response.tipoUsuario().nome()).isEqualTo("PROPRIETARIO");
    }

    @Test
    @DisplayName("Deve converter para RestauranteResponse com sub-relacionamentos nulos quando as portas retornarem vazio")
    void deveConverterParaRestauranteResponseComRelacionamentosNulos() {
        // Arrange
        Restaurante restaurante = new Restaurante(1L, "Sabor das Américas", 10L, 20L, horarioFuncionamento, 30L);

        // Configura todas as portas de saída para retornar Optional vazio, ativando as ramificações .orElse(null)
        when(enderecoRepositoryPort.findById(10L)).thenReturn(Optional.empty());
        when(tipoCozinhaRepositoryPort.findById(20L)).thenReturn(Optional.empty());
        when(tipoUsuarioRepositoryPort.findById(30L)).thenReturn(Optional.empty());

        // Act
        RestauranteResponse response = restauranteResponseAssembler.toResponse(restaurante);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Sabor das Américas");

        // Verifica se as ramificações de erro/vazio foram tratadas corretamente
        assertThat(response.endereco()).isNull();
        assertThat(response.tipoCozinha()).isNull();
        assertThat(response.tipoUsuario()).isNull();
    }
}
