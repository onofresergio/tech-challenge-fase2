package br.com.fiap.techchallenge.infrastructure.controller;

import br.com.fiap.techchallenge.domain.model.Endereco;
import br.com.fiap.techchallenge.infrastructure.controller.request.EnderecoRequest;
import br.com.fiap.techchallenge.usecase.port.input.EnderecoCommandUseCase;
import br.com.fiap.techchallenge.usecase.port.input.EnderecoQueryUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//
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

@WebMvcTest(EnderecoController.class)
public class EnderecoControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private ObjectMapper objectMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private EnderecoCommandUseCase enderecoCommandUseCase;

    @MockitoBean
    private EnderecoQueryUseCase enderecoQueryUseCase;

    private final Endereco enderecoDomain = new Endereco(1L, "Av. Paulista", "1000", "São Paulo", "SP", "01311-100");
    private final EnderecoRequest enderecoRequestValido = new EnderecoRequest("Av. Paulista", "1000", "São Paulo", "SP", "01311-100");

    // ==========================================
    // TESTES: POST /api/v2/enderecos
    // ==========================================

    @Test
    @DisplayName("create: Deve retornar 201 Created e o JSON estruturado ao cadastrar um endereço válido")
    void createDeveRetornarStatusCreatedEEnderecoResponse() throws Exception {
        // Arrange
        when(enderecoCommandUseCase.create(any(), any(), any(), any(), any())).thenReturn(enderecoDomain);

        // Act & Assert
        mockMvc.perform(post("/api/v2/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enderecoRequestValido)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.rua").value("Av. Paulista"))
                .andExpect(jsonPath("$.numero").value("1000"))
                .andExpect(jsonPath("$.cidade").value("São Paulo"))
                .andExpect(jsonPath("$.estado").value("SP"))
                .andExpect(jsonPath("$.cep").value("01311-100"));

        verify(enderecoCommandUseCase, times(1)).create("Av. Paulista", "1000", "São Paulo", "SP", "01311-100");
    }

    // ==========================================
    // TESTES: PUT /api/v2/enderecos/{id}
    // ==========================================

    @Test
    @DisplayName("update: Deve retornar 200 OK e os dados alterados do EnderecoResponse ao atualizar com sucesso")
    void updateDeveRetornarStatusOkEEnderecoResponseAtualizado() throws Exception {
        // Arrange
        Long id = 1L;
        when(enderecoCommandUseCase.update(eq(id), any(), any(), any(), any(), any())).thenReturn(enderecoDomain);

        // Act & Assert
        mockMvc.perform(put("/api/v2/enderecos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enderecoRequestValido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.rua").value("Av. Paulista"))
                .andExpect(jsonPath("$.numero").value("1000"))
                .andExpect(jsonPath("$.cidade").value("São Paulo"))
                .andExpect(jsonPath("$.estado").value("SP"))
                .andExpect(jsonPath("$.cep").value("01311-100"));

        verify(enderecoCommandUseCase, times(1)).update(id, "Av. Paulista", "1000", "São Paulo", "SP", "01311-100");
    }

    // ==========================================
    // TESTES: GET /api/v2/enderecos/{id}
    // ==========================================

    @Test
    @DisplayName("getById: Deve retornar 200 OK e o JSON do EnderecoResponse correspondente ao ID informado")
    void getByIdDeveRetornarStatusOkEEnderecoResponse() throws Exception {
        // Arrange
        Long id = 1L;
        when(enderecoQueryUseCase.findById(id)).thenReturn(enderecoDomain);

        // Act & Assert
        mockMvc.perform(get("/api/v2/enderecos/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.rua").value("Av. Paulista"))
                .andExpect(jsonPath("$.numero").value("1000"))
                .andExpect(jsonPath("$.cidade").value("São Paulo"))
                .andExpect(jsonPath("$.estado").value("SP"))
                .andExpect(jsonPath("$.cep").value("01311-100"));

        verify(enderecoQueryUseCase, times(1)).findById(id);
    }

    // ==========================================
    // TESTES: GET /api/v2/enderecos
    // ==========================================

    @Test
    @DisplayName("getAll: Deve retornar 200 OK e a lista mapeada de objetos EnderecoResponse")
    void getAllDeveRetornarStatusOkEListaDeEnderecoResponse() throws Exception {
        // Arrange
        List<Endereco> lista = List.of(enderecoDomain);
        when(enderecoQueryUseCase.findAll()).thenReturn(lista);

        // Act & Assert
        mockMvc.perform(get("/api/v2/enderecos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].rua").value("Av. Paulista"))
                .andExpect(jsonPath("$[0].numero").value("1000"))
                .andExpect(jsonPath("$[0].cidade").value("São Paulo"))
                .andExpect(jsonPath("$[0].estado").value("SP"))
                .andExpect(jsonPath("$[0].cep").value("01311-100"));

        verify(enderecoQueryUseCase, times(1)).findAll();
    }

    // ==========================================
    // TESTES: DELETE /api/v2/enderecos/{id}
    // ==========================================

    @Test
    @DisplayName("delete: Deve retornar 204 No Content ao realizar a exclusão por ID com sucesso")
    void deleteDeveRetornarStatusNoContent() throws Exception {
        // Arrange
        Long id = 1L;
        // CORREÇÃO: Sintaxe correta do Mockito para métodos que retornam void
        doNothing().when(enderecoCommandUseCase).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api/v2/enderecos/{id}", id))
                .andExpect(status().isNoContent());

        verify(enderecoCommandUseCase, times(1)).delete(id);
    }

}
