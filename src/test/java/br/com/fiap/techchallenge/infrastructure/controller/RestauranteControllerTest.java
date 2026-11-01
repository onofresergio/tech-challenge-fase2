package br.com.fiap.techchallenge.infrastructure.controller;

import br.com.fiap.techchallenge.domain.model.Restaurante;
import br.com.fiap.techchallenge.infrastructure.controller.request.AtualizarRestauranteRequest;
import br.com.fiap.techchallenge.infrastructure.controller.request.CriarRestauranteRequest;
import br.com.fiap.techchallenge.infrastructure.controller.response.RestauranteResponse;
import br.com.fiap.techchallenge.usecase.assembler.RestauranteResponseAssembler;
import br.com.fiap.techchallenge.usecase.port.input.CriarRestauranteUseCase;
import br.com.fiap.techchallenge.usecase.port.input.DeleteRestauranteUseCase;
import br.com.fiap.techchallenge.usecase.port.input.RestauranteQueryUseCase;
import br.com.fiap.techchallenge.usecase.port.input.UpdateRestauranteUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
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

@WebMvcTest(RestauranteController.class)
public class RestauranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CriarRestauranteUseCase criarRestauranteUseCase;

    @MockitoBean
    private RestauranteQueryUseCase restauranteQueryUseCase;

    @MockitoBean
    private UpdateRestauranteUseCase updateRestauranteUseCase;

    @MockitoBean
    private DeleteRestauranteUseCase deleteRestauranteUseCase;

    @MockitoBean
    private RestauranteResponseAssembler restauranteResponseAssembler;

    private final LocalDateTime horarioFuncionamento = LocalDateTime.of(2026, 10, 12, 18, 0);
    private final Restaurante restauranteDomain = new Restaurante(1L, "Fiap Burguer", 10L, 20L, horarioFuncionamento, 4L);
    private final RestauranteResponse restauranteResponse = new RestauranteResponse(1L, "Fiap Burguer", horarioFuncionamento, null, null, null);

    @BeforeEach
    void setUp() {
        // Registra o módulo JSR310 para o Jackson conseguir serializar LocalDateTime sem estourar erros de tipo
        objectMapper.registerModule(new JavaTimeModule());
    }

    // ==========================================
    // TESTES: POST /api/v2/restaurantes
    // ==========================================

    @Test
    @DisplayName("create: Deve retornar 201 Created e o JSON do RestauranteResponse ao cadastrar com sucesso")
    void createDeveRetornarStatusCreatedERestauranteResponse() throws Exception {
        // Arrange
        CriarRestauranteRequest request = new CriarRestauranteRequest("Fiap Burguer", 10L, 20L, horarioFuncionamento, 4L);

        when(criarRestauranteUseCase.execute(any(), any(), any(), any(), any())).thenReturn(restauranteDomain);
        when(restauranteResponseAssembler.toResponse(restauranteDomain)).thenReturn(restauranteResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v2/restaurantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Fiap Burguer"));

        verify(criarRestauranteUseCase, times(1)).execute("Fiap Burguer", 10L, 20L, horarioFuncionamento, 4L);
        verify(restauranteResponseAssembler, times(1)).toResponse(restauranteDomain);
    }

    // ==========================================
    // TESTES: PUT /api/v2/restaurantes/{id}
    // ==========================================

    @Test
    @DisplayName("update: Deve retornar 200 OK e mapear as propriedades alteradas do restaurante")
    void updateDeveRetornarStatusOkERestauranteResponseAtualizado() throws Exception {
        // Arrange
        Long id = 1L;
        AtualizarRestauranteRequest request = new AtualizarRestauranteRequest("Fiap Burguer Alterado", 1L, 1L, horarioFuncionamento, 4L);
        Restaurante restauranteAlterado = new Restaurante(id, "Fiap Burguer Alterado", 10L, 20L, horarioFuncionamento, 4L);
        RestauranteResponse responseAlterado = new RestauranteResponse(id, "Fiap Burguer Alterado", horarioFuncionamento, null, null, null);

        when(updateRestauranteUseCase.execute(eq(id), any(), any(), any(), any(), any())).thenReturn(restauranteAlterado);
        when(restauranteResponseAssembler.toResponse(restauranteAlterado)).thenReturn(responseAlterado);

        // Act & Assert
        mockMvc.perform(put("/api/v2/restaurantes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Fiap Burguer Alterado"));

        verify(updateRestauranteUseCase, times(1)).execute(id, "Fiap Burguer Alterado", 1L, 1L, horarioFuncionamento, 4L);
        verify(restauranteResponseAssembler, times(1)).toResponse(restauranteAlterado);
    }

    // ==========================================
    // TESTES: GET /api/v2/restaurantes/{id}
    // ==========================================

    @Test
    @DisplayName("getById: Deve retornar 200 OK e carregar os relacionamentos convertidos pelo assembler")
    void getByIdDeveRetornarStatusOkERestauranteResponse() throws Exception {
        // Arrange
        Long id = 1L;
        when(restauranteQueryUseCase.buscarPorId(id)).thenReturn(restauranteDomain);
        when(restauranteResponseAssembler.toResponse(restauranteDomain)).thenReturn(restauranteResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v2/restaurantes/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Fiap Burguer"));

        verify(restauranteQueryUseCase, times(1)).buscarPorId(id);
        verify(restauranteResponseAssembler, times(1)).toResponse(restauranteDomain);
    }

    // ==========================================
    // TESTES: GET /api/v2/restaurantes
    // ==========================================

    @Test
    @DisplayName("getAll: Deve retornar 200 OK e a lista mapeada contendo indexação contra JSONArray")
    void getAllDeveRetornarStatusOkEListaDeRestauranteResponse() throws Exception {
        // Arrange
        List<Restaurante> lista = List.of(restauranteDomain);
        when(restauranteQueryUseCase.listar()).thenReturn(lista);
        when(restauranteResponseAssembler.toResponse(restauranteDomain)).thenReturn(restauranteResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v2/restaurantes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                // Indexação corrigida para ler coleções sem estourar PathNotFoundException
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Fiap Burguer"));

        verify(restauranteQueryUseCase, times(1)).listar();
        verify(restauranteResponseAssembler, times(1)).toResponse(restauranteDomain);
    }

    // ==========================================
    // TESTES: DELETE /api/v2/restaurantes/{id}
    // ==========================================

    @Test
    @DisplayName("delete: Deve retornar 204 No Content ao remover o restaurante por ID com sucesso")
    void deleteDeveRetornarStatusNoContent() throws Exception {
        // Arrange
        Long id = 1L;
        // Sintaxe fluida do Mockito para tratar métodos de casos de uso sem retorno (void)
        doNothing().when(deleteRestauranteUseCase).execute(id);

        // Act & Assert
        mockMvc.perform(delete("/api/v2/restaurantes/{id}", id))
                .andExpect(status().isNoContent());

        verify(deleteRestauranteUseCase, times(1)).execute(id);
    }
}
