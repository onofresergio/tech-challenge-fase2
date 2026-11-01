package br.com.fiap.techchallenge.infrastructure.controller;

import br.com.fiap.techchallenge.domain.model.Usuario;
import br.com.fiap.techchallenge.infrastructure.controller.request.AtualizarSenhaRequest;
import br.com.fiap.techchallenge.infrastructure.controller.request.UsuarioRequest;
import br.com.fiap.techchallenge.infrastructure.controller.request.UsuarioUpdateRequest;
import br.com.fiap.techchallenge.infrastructure.controller.response.UsuarioResponse;
import br.com.fiap.techchallenge.usecase.assembler.UsuarioResponseAssembler;
import br.com.fiap.techchallenge.usecase.port.input.UsuarioCommandUseCase;
import br.com.fiap.techchallenge.usecase.port.input.UsuarioQueryUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Instanciação direta do Jackson que resolve o problema de UnsatisfiedDependencyException
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UsuarioCommandUseCase usuarioCommandUseCase;

    @MockitoBean
    private UsuarioQueryUseCase usuarioQueryUseCase;

    @MockitoBean
    private UsuarioResponseAssembler usuarioResponseAssembler;

    private final LocalDateTime dataPadrao = LocalDateTime.now();
    private final Usuario usuarioDomain = new Usuario(1L, "Onofre", "onofre@teste.com.br", "hash", "onofre", dataPadrao, 10L, 20L);
    private final UsuarioResponse usuarioResponse = new UsuarioResponse(1L, "Onofre", "onofre@teste.com.br", "onofre", null, null);

    // ==========================================
    // TESTES: POST /api/v2/usuarios
    // ==========================================

    @Test
    @DisplayName("create: Deve retornar 201 Created e o JSON do UsuarioResponse ao cadastrar com sucesso")
    void createDeveRetornarStatusCreatedEUsuarioResponse() throws Exception {
        // Arrange
        UsuarioRequest request = new UsuarioRequest("Onofre", "onofre@teste.com.br", "senha123", "onofre", 10L, 20L);

        when(usuarioCommandUseCase.create(any(), any(), any(), any(), any(), any())).thenReturn(usuarioDomain);
        when(usuarioResponseAssembler.toResponse(usuarioDomain)).thenReturn(usuarioResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v2/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Onofre"))
                .andExpect(jsonPath("$.email").value("onofre@teste.com.br"));

        verify(usuarioCommandUseCase, times(1)).create("Onofre", "onofre@teste.com.br", "senha123", "onofre", 10L, 20L);
        verify(usuarioResponseAssembler, times(1)).toResponse(usuarioDomain);
    }

    // ==========================================
    // TESTES: PUT /api/v2/usuarios/{id}
    // ==========================================

    @Test
    @DisplayName("update: Deve retornar 200 OK e os dados alterados do usuário cadastrado")
    void updateDeveRetornarStatusOkEUsuarioResponseAtualizado() throws Exception {
        // Arrange
        Long id = 1L;
        UsuarioUpdateRequest request = new UsuarioUpdateRequest("Onofre Alterado", "onofre_new@email.com", "onofre_new", 11L, 21L);
        Usuario usuarioAlterado = new Usuario(id, "Onofre Alterado", "onofre_new@email.com", "hash", "onofre_new", dataPadrao, 11L, 21L);
        UsuarioResponse usuarioResponseAlterado = new UsuarioResponse(id, "Onofre Alterado", "onofre_new@email.com", "onofre_new", null, null);

        when(usuarioCommandUseCase.update(eq(id), any(), any(), any(), any(), any())).thenReturn(usuarioAlterado);
        when(usuarioResponseAssembler.toResponse(usuarioAlterado)).thenReturn(usuarioResponseAlterado);

        // Act & Assert
        mockMvc.perform(put("/api/v2/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Onofre Alterado"))
                .andExpect(jsonPath("$.email").value("onofre_new@email.com"));

        verify(usuarioCommandUseCase, times(1)).update(id, "Onofre Alterado", "onofre_new@email.com", "onofre_new", 11L, 21L);
        verify(usuarioResponseAssembler, times(1)).toResponse(usuarioAlterado);
    }

    // ==========================================
    // TESTES: PUT /api/v2/usuarios/{id}/senha
    // ==========================================

    @Test
    @DisplayName("updatePassword: Deve retornar 200 OK e o JSON de resposta ao atualizar a senha com sucesso")
    void updatePasswordDeveRetornarStatusOkEUsuarioResponse() throws Exception {
        // Arrange
        Long id = 1L;
        AtualizarSenhaRequest request = new AtualizarSenhaRequest("novaSenha123");

        when(usuarioCommandUseCase.changePassword(eq(id), any())).thenReturn(usuarioDomain);
        when(usuarioResponseAssembler.toResponse(usuarioDomain)).thenReturn(usuarioResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v2/usuarios/{id}/senha", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(usuarioCommandUseCase, times(1)).changePassword(id, "novaSenha123");
        verify(usuarioResponseAssembler, times(1)).toResponse(usuarioDomain);
    }

    // ==========================================
    // TESTES: GET /api/v2/usuarios/{id}
    // ==========================================

    @Test
    @DisplayName("getById: Deve retornar 200 OK e mapear o usuário correspondente ao ID informado")
    void getByIdDeveRetornarStatusOkEUsuarioResponse() throws Exception {
        // Arrange
        Long id = 1L;
        when(usuarioQueryUseCase.findById(id)).thenReturn(usuarioDomain);
        when(usuarioResponseAssembler.toResponse(usuarioDomain)).thenReturn(usuarioResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v2/usuarios/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Onofre"));

        verify(usuarioQueryUseCase, times(1)).findById(id);
        verify(usuarioResponseAssembler, times(1)).toResponse(usuarioDomain);
    }

    // ==========================================
    // TESTES: GET /api/v2/usuarios
    // ==========================================

    @Test
    @DisplayName("getAll: Deve retornar 200 OK e a lista mapeada de todos os usuários")
    void getAllDeveRetornarStatusOkEListaDeUsuarioResponse() throws Exception {
        // Arrange
        List<Usuario> lista = List.of(usuarioDomain);
        when(usuarioQueryUseCase.findAll()).thenReturn(lista);
        when(usuarioResponseAssembler.toResponse(usuarioDomain)).thenReturn(usuarioResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v2/usuarios")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                // CORREÇÃO: Utilizando a indexação explícita de array para evitar o PathNotFoundException
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Onofre"));

        verify(usuarioQueryUseCase, times(1)).findAll();
        verify(usuarioResponseAssembler, times(1)).toResponse(usuarioDomain);
    }

    // ==========================================
    // TESTES: GET /api/v2/usuarios/nome/{nome}
    // ==========================================

    @Test
    @DisplayName("getByNomeContaining: Deve retornar 200 OK e filtrar a lista de usuários pelo nome informado")
    void getByNomeContainingDeveRetornarStatusOkEListaFiltrada() throws Exception {
        // Arrange
        String nomeBusca = "Onofre";
        List<Usuario> lista = List.of(usuarioDomain);
        when(usuarioQueryUseCase.findByNomeContaining(nomeBusca)).thenReturn(lista);
        when(usuarioResponseAssembler.toResponse(usuarioDomain)).thenReturn(usuarioResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v2/usuarios/nome/{nome}", nomeBusca)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                // CORREÇÃO: Utilizando a indexação explícita de array para evitar o PathNotFoundException
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Onofre"));

        verify(usuarioQueryUseCase, times(1)).findByNomeContaining(nomeBusca);
        verify(usuarioResponseAssembler, times(1)).toResponse(usuarioDomain);
    }

    // ==========================================
    // TESTES: DELETE /api/v2/usuarios/{id}
    // ==========================================

    @Test
    @DisplayName("delete: Deve retornar 204 No Content ao remover o usuário por ID com sucesso")
    void deleteDeveRetornarStatusNoContent() throws Exception {
        // Arrange
        Long id = 1L;
        // Estrutura fluida padrão do Mockito para mapear execução sem retorno (void)
        doNothing().when(usuarioCommandUseCase).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api/v2/usuarios/{id}", id))
                .andExpect(status().isNoContent());

        verify(usuarioCommandUseCase, times(1)).delete(id);
    }
}
