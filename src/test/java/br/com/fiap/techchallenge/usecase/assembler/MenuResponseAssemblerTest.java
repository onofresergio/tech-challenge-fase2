package br.com.fiap.techchallenge.usecase.assembler;

import br.com.fiap.techchallenge.domain.model.Endereco;
import br.com.fiap.techchallenge.domain.model.Menu;
import br.com.fiap.techchallenge.domain.model.Restaurante;
import br.com.fiap.techchallenge.domain.model.TipoCozinha;
import br.com.fiap.techchallenge.domain.model.TipoUsuario;
import br.com.fiap.techchallenge.infrastructure.controller.response.MenuResponse;
import br.com.fiap.techchallenge.usecase.port.output.EnderecoRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.RestauranteRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.TipoCozinhaRepositoryPort;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuResponseAssemblerTest {

    @Mock
    private RestauranteRepositoryPort restauranteRepositoryPort;

    @Mock
    private EnderecoRepositoryPort enderecoRepositoryPort;

    @Mock
    private TipoCozinhaRepositoryPort tipoCozinhaRepositoryPort;

    @Mock
    private TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort;

    @InjectMocks
    private MenuResponseAssembler menuResponseAssembler;

    private final LocalDateTime horarioFuncionamento = LocalDateTime.of(2026, 10, 12, 12, 0);

    @Test
    @DisplayName("Deve converter para MenuResponse com todos os relacionamentos preenchidos (Cenário Completo)")
    void deveConverterParaMenuResponseCompleto() {
        // Arrange
        Menu menu = new Menu(1L, "Pizza", "Muzzarella", new BigDecimal("50.00"), true, "pizza.jpg", 10L);
        Restaurante restaurante = new Restaurante(10L, "Pizzaria Fiap", 20L, 30L, horarioFuncionamento, 40L);
        Endereco endereco = new Endereco(20L, "Rua A", "123", "São Paulo", "SP", "01234-567");
        TipoCozinha tipoCozinha = new TipoCozinha(30L, "Italiana");
        TipoUsuario tipoUsuario = new TipoUsuario(40L, "GERENTE");

        when(restauranteRepositoryPort.findById(10L)).thenReturn(Optional.of(restaurante));
        when(enderecoRepositoryPort.findById(20L)).thenReturn(Optional.of(endereco));
        when(tipoCozinhaRepositoryPort.findById(30L)).thenReturn(Optional.of(tipoCozinha));
        when(tipoUsuarioRepositoryPort.findById(40L)).thenReturn(Optional.of(tipoUsuario));

        // Act
        MenuResponse response = menuResponseAssembler.toResponse(menu);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Pizza");
        assertThat(response.restaurante()).isNotNull();
        assertThat(response.restaurante().id()).isEqualTo(10L);
        assertThat(response.restaurante().nome()).isEqualTo("Pizzaria Fiap");
        assertThat(response.restaurante().endereco()).isNotNull();
        assertThat(response.restaurante().endereco().id()).isEqualTo(20L);
        assertThat(response.restaurante().tipoCozinha()).isNotNull();
        assertThat(response.restaurante().tipoCozinha().nome()).isEqualTo("Italiana");
        assertThat(response.restaurante().tipoUsuario()).isNotNull();
        assertThat(response.restaurante().tipoUsuario().nome()).isEqualTo("GERENTE");
    }

    @Test
    @DisplayName("Deve retornar MenuResponse com restaurante nulo caso o restaurante não seja encontrado")
    void deveRetornarMenuResponseComRestauranteNulo() {
        // Arrange
        Menu menu = new Menu(1L, "Pizza", "Muzzarella", new BigDecimal("50.00"), true, "pizza.jpg", 10L);

        when(restauranteRepositoryPort.findById(10L)).thenReturn(Optional.empty());

        // Act
        MenuResponse response = menuResponseAssembler.toResponse(menu);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.restaurante()).isNull(); // Ativa o .orElse(null) do restaurante
    }

    @Test
    @DisplayName("Deve mapear sub-relacionamentos como nulos se as portas retornarem vazio")
    void deveMapearSubRelacionamentosComoNuloQuandoNaoEncontrados() {
        // Arrange
        Menu menu = new Menu(1L, "Pizza", "Muzzarella", new BigDecimal("50.00"), true, "pizza.jpg", 10L);
        Restaurante restaurante = new Restaurante(10L, "Pizzaria Fiap", 20L, 30L, horarioFuncionamento, 40L);

        when(restauranteRepositoryPort.findById(10L)).thenReturn(Optional.of(restaurante));
        when(enderecoRepositoryPort.findById(20L)).thenReturn(Optional.empty());
        when(tipoCozinhaRepositoryPort.findById(30L)).thenReturn(Optional.empty());
        when(tipoUsuarioRepositoryPort.findById(40L)).thenReturn(Optional.empty());

        // Act
        MenuResponse response = menuResponseAssembler.toResponse(menu);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.restaurante()).isNotNull();
        assertThat(response.restaurante().id()).isEqualTo(10L);
        assertThat(response.restaurante().endereco()).isNull();      // Ativa .orElse(null) do endereço
        assertThat(response.restaurante().tipoCozinha()).isNull();   // Ativa .orElse(null) da cozinha
        assertThat(response.restaurante().tipoUsuario()).isNull();   // Ativa .orElse(null) do usuário
    }
}
