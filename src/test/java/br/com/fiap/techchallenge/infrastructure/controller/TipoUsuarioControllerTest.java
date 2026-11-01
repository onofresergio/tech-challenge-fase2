package br.com.fiap.techchallenge.infrastructure.controller;

import br.com.fiap.techchallenge.domain.model.TipoUsuario;
import br.com.fiap.techchallenge.infrastructure.controller.request.CriarTipoUsuarioRequest;
import br.com.fiap.techchallenge.infrastructure.controller.request.TipoUsuarioRequest;
import br.com.fiap.techchallenge.usecase.port.input.CriarTipoUsuarioUseCase;
import br.com.fiap.techchallenge.usecase.port.input.TipoUsuarioCommandUseCase;
import br.com.fiap.techchallenge.usecase.port.input.TipoUsuarioQueryUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TipoUsuarioController.class)
public class TipoUsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Instanciação direta do Jackson que resolve o problema de UnsatisfiedDependencyException
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CriarTipoUsuarioUseCase criarTipoUsuarioUseCase;

    @MockitoBean
    private TipoUsuarioCommandUseCase tipoUsuarioCommandUseCase;

    @MockitoBean
    private TipoUsuarioQueryUseCase tipoUsuarioQueryUseCase;

    private final TipoUsuario tipoUsuarioDomain = new TipoUsuario(1L, "CLIENTE");
    private final CriarTipoUsuarioRequest criarTipoUsuarioRequest = new CriarTipoUsuarioRequest("CLIENTE");
    private final TipoUsuarioRequest tipoUsuarioRequest = new TipoUsuarioRequest(1L, "CLIENTE");

    // ==========================================
    // TESTES: POST /api/v2/tipo-usuario
    // ==========================================

    @Test
    @DisplayName("create: Deve retornar 201 Created e o JSON do TipoUsuarioResponse ao cadastrar com sucesso")
    void createDeveRetornarStatusCreatedETipoUsuarioResponse() throws Exception {
        // Arrange
        when(criarTipoUsuarioUseCase.create("CLIENTE")).thenReturn(tipoUsuarioDomain);

        // Act & Assert
        mockMvc.perform(post("/api/v2/tipo-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarTipoUsuarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("CLIENTE"));

        verify(criarTipoUsuarioUseCase, times(1)).create("CLIENTE");
    }

    // ==========================================
    // TESTES: PUT /api/v2/tipo-usuario/{id}
    // ==========================================

    @Test
    @DisplayName("update: Deve retornar 200 OK e o JSON atualizado ao alterar o tipo de usuário com sucesso")
    void updateDeveRetornarStatusOkETipoUsuarioResponseAtualizado() throws Exception {
        // Arrange
        Long id = 1L;
        when(tipoUsuarioCommandUseCase.update(eq(id), any())).thenReturn(tipoUsuarioDomain);

        // Act & Assert
        mockMvc.perform(put("/api/v2/tipo-usuario/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipoUsuarioRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("CLIENTE"));

        verify(tipoUsuarioCommandUseCase, times(1)).update(id, "CLIENTE");
    }

    // ==========================================
    // TESTES: GET /api/v2/tipo-usuario/{id}
    // ==========================================

    @Test
    @DisplayName("getById: Deve retornar 200 OK e o JSON correspondente ao ID informado")
    void getByIdDeveRetornarStatusOkETipoUsuarioResponse() throws Exception {
        // Arrange
        Long id = 1L;
        when(tipoUsuarioQueryUseCase.findById(id)).thenReturn(tipoUsuarioDomain);

        // Act & Assert
        mockMvc.perform(get("/api/v2/tipo-usuario/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("CLIENTE"));

        verify(tipoUsuarioQueryUseCase, times(1)).findById(id);
    }

    // ==========================================
    // TESTES: GET /api/v2/tipo-usuario
    // ==========================================

    @Test
    @DisplayName("getAll: Deve retornar 200 OK e a lista mapeada de objetos TipoUsuarioResponse")
    void getAllDeveRetornarStatusOkEListaDeTipoUsuarioResponse() throws Exception {
        // Arrange
        List<TipoUsuario> lista = List.of(tipoUsuarioDomain);
        when(tipoUsuarioQueryUseCase.findAll()).thenReturn(lista);

        // Act & Assert
        mockMvc.perform(get("/api/v2/tipo-usuario")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                // CORREÇÃO: Utilizando a indexação explícita $[0] para extrair os dados do array JSON
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("CLIENTE"));

        verify(tipoUsuarioQueryUseCase, times(1)).findAll();
    }

    // ==========================================
    // TESTES: DELETE /api/v2/tipo-usuario/{id}
    // ==========================================

    @Test
    @DisplayName("delete: Deve retornar 204 No Content ao realizar a exclusão por ID com sucesso")
    void deleteDeveRetornarStatusNoContent() throws Exception {
        // Arrange
        Long id = 1L;
        // Sintaxe fluida do Mockito para tratar métodos de casos de uso sem retorno (void)
        doNothing().when(tipoUsuarioCommandUseCase).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api/v2/tipo-usuario/{id}", id))
                .andExpect(status().isNoContent());

        verify(tipoUsuarioCommandUseCase, times(1)).delete(id);
    }
}
