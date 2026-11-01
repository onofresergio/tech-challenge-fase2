package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.model.TipoUsuario;
import br.com.fiap.techchallenge.usecase.port.output.TipoUsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CriarTipoUsuarioInteractorTest {

    @Mock
    private TipoUsuarioRepositoryPort tipoUsuarioRepositoryPort;

    @InjectMocks
    private CriarTipoUsuarioInteractor criarTipoUsuarioInteractor;

    @Test
    @DisplayName("Deve criar um TipoUsuario com sucesso chamando o repositório")
    void deveCriarTipoUsuarioComSucesso() {
        // Arrange
        String nomeEsperado = "ADMINISTRADOR";
        TipoUsuario tipoUsuarioSalvo = new TipoUsuario(1L, nomeEsperado);

        when(tipoUsuarioRepositoryPort.save(any(TipoUsuario.class))).thenReturn(tipoUsuarioSalvo);

        // Act
        TipoUsuario resultado = criarTipoUsuarioInteractor.create(nomeEsperado);

        // Assert
        assertNotNull(resultado);
        assertSame(tipoUsuarioSalvo, resultado, "O interactor deve retornar exatamente o objeto retornado pelo repositório");

        // Verifica se o repositório foi chamado exatamente uma vez
        verify(tipoUsuarioRepositoryPort, times(1)).save(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve passar o TipoUsuario com os parâmetros corretos para o repositório")
    void deveInstanciarTipoUsuarioCorretamente() {
        // Arrange
        String nomeInput = "CLIENTE";
        ArgumentCaptor<TipoUsuario> tipoUsuarioCaptor = ArgumentCaptor.forClass(TipoUsuario.class);

        when(tipoUsuarioRepositoryPort.save(any(TipoUsuario.class))).thenReturn(new TipoUsuario(2L, nomeInput));

        // Act
        criarTipoUsuarioInteractor.create(nomeInput);

        // Assert
        verify(tipoUsuarioRepositoryPort).save(tipoUsuarioCaptor.capture());
        TipoUsuario tipoUsuarioEnviado = tipoUsuarioCaptor.getValue();

        assertEquals(new TipoUsuario(null, nomeInput), tipoUsuarioEnviado);
    }

    @Test
    @DisplayName("Deve propagar exceção caso o repositório falhe")
    void devePropagarExcecaoQuandoRepositorioFalhar() {
        // Arrange
        String nomeInput = "GERENTE";
        when(tipoUsuarioRepositoryPort.save(any(TipoUsuario.class)))
                .thenThrow(new RuntimeException("Erro ao conectar ao banco de dados"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            criarTipoUsuarioInteractor.create(nomeInput);
        });

        verify(tipoUsuarioRepositoryPort, times(1)).save(any(TipoUsuario.class));
    }

}
