package br.com.fiap.techchallenge.infrastructure.controller;

import br.com.fiap.techchallenge.domain.model.Menu;
import br.com.fiap.techchallenge.infrastructure.controller.request.AtualizarMenuRequest;
import br.com.fiap.techchallenge.infrastructure.controller.request.CriarMenuRequest;
import br.com.fiap.techchallenge.infrastructure.controller.response.MenuResponse;
import br.com.fiap.techchallenge.usecase.assembler.MenuResponseAssembler;
import br.com.fiap.techchallenge.usecase.port.input.CriarMenuUseCase;
import br.com.fiap.techchallenge.usecase.port.input.DeleteMenuUseCase;
import br.com.fiap.techchallenge.usecase.port.input.MenuQueryUseCase;
import br.com.fiap.techchallenge.usecase.port.input.UpdateMenuUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuController.class)
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Instanciação direta do Jackson que resolve o problema de UnsatisfiedDependencyException
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CriarMenuUseCase criarMenuUseCase;

    @MockitoBean
    private MenuQueryUseCase menuQueryUseCase;

    @MockitoBean
    private UpdateMenuUseCase updateMenuUseCase;

    @MockitoBean
    private DeleteMenuUseCase deleteMenuUseCase;

    @MockitoBean
    private MenuResponseAssembler menuResponseAssembler;

    private final Menu menuDomain = new Menu(1L, "Pizza", "Muzzarella", new BigDecimal("50.00"), true, "pizza.jpg", 10L);
    private final MenuResponse menuResponse = new MenuResponse(1L, "Pizza", "Muzzarella", new BigDecimal("50.00"), true, "pizza.jpg", null);

    // ==========================================
    // TESTES: POST /api/v2/menus
    // ==========================================

    @Test
    @DisplayName("create: Deve retornar 201 Created e o JSON do MenuResponse ao cadastrar com sucesso")
    void createDeveRetornarStatusCreatedEMenuResponse() throws Exception {
        // Arrange
        CriarMenuRequest request = new CriarMenuRequest("Pizza", "Muzzarella", new BigDecimal("50.00"), true, "pizza.jpg", 10L);

        when(criarMenuUseCase.execute(any(), any(), any(), anyBoolean(), any(), any())).thenReturn(menuDomain);
        when(menuResponseAssembler.toResponse(menuDomain)).thenReturn(menuResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v2/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Pizza"))
                .andExpect(jsonPath("$.valorPrato").value(50.00));

        verify(criarMenuUseCase, times(1)).execute("Pizza", "Muzzarella", new BigDecimal("50.00"), true, "pizza.jpg", 10L);
        verify(menuResponseAssembler, times(1)).toResponse(menuDomain);
    }

    // ==========================================
    // TESTES: PUT /api/v2/menus/{id}
    // ==========================================

    @Test
    @DisplayName("update: Deve retornar 200 OK e os dados alterados do menu convertido pelo assembler")
    void updateDeveRetornarStatusOkEMenuResponseAtualizado() throws Exception {
        // Arrange
        Long id = 1L;
        AtualizarMenuRequest request = new AtualizarMenuRequest("Pizza Alterada", "Muzzarella Premium", new BigDecimal("55.00"), true, "pizza2.jpg");
        Menu menuAlterado = new Menu(id, "Pizza Alterada", "Muzzarella Premium", new BigDecimal("55.00"), true, "pizza2.jpg", 10L);
        MenuResponse menuResponseAlterado = new MenuResponse(id, "Pizza Alterada", "Muzzarella Premium", new BigDecimal("55.00"), true, "pizza2.jpg", null);

        when(updateMenuUseCase.execute(eq(id), any(), any(), any(), anyBoolean(), any())).thenReturn(menuAlterado);
        when(menuResponseAssembler.toResponse(menuAlterado)).thenReturn(menuResponseAlterado);

        // Act & Assert
        mockMvc.perform(put("/api/v2/menus/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Pizza Alterada"))
                .andExpect(jsonPath("$.valorPrato").value(55.00));

        verify(updateMenuUseCase, times(1)).execute(id, "Pizza Alterada", "Muzzarella Premium", new BigDecimal("55.00"), true, "pizza2.jpg");
        verify(menuResponseAssembler, times(1)).toResponse(menuAlterado);
    }

    // ==========================================
    // TESTES: GET /api/v2/menus/{id}
    // ==========================================

    @Test
    @DisplayName("getById: Deve retornar 200 OK e mapear o menu correspondente ao ID informado")
    void getByIdDeveRetornarStatusOkEMenuResponse() throws Exception {
        // Arrange
        Long id = 1L;
        when(menuQueryUseCase.buscarPorId(id)).thenReturn(menuDomain);
        when(menuResponseAssembler.toResponse(menuDomain)).thenReturn(menuResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v2/menus/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Pizza"));

        verify(menuQueryUseCase, times(1)).buscarPorId(id);
        verify(menuResponseAssembler, times(1)).toResponse(menuDomain);
    }

    // ==========================================
    // TESTES: GET /api/v2/menus
    // ==========================================

    @Test
    @DisplayName("getAll: Deve retornar 200 OK e a lista mapeada de objetos MenuResponse de todos os pratos")
    void getAllDeveRetornarStatusOkEListaDeMenuResponse() throws Exception {
        // Arrange
        List<Menu> lista = List.of(menuDomain);
        when(menuQueryUseCase.listar()).thenReturn(lista);
        when(menuResponseAssembler.toResponse(menuDomain)).thenReturn(menuResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v2/menus")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Pizza"));

        verify(menuQueryUseCase, times(1)).listar();
        verify(menuResponseAssembler, times(1)).toResponse(menuDomain);
    }

    // ==========================================
    // TESTES: DELETE /api/v2/menus/{id}
    // ==========================================

    @Test
    @DisplayName("delete: Deve retornar 204 No Content ao realizar a exclusão por ID com sucesso")
    void deleteDeveRetornarStatusNoContent() throws Exception {
        // Arrange
        Long id = 1L;
        // Sintaxe correta para mockar métodos de casos de uso com assinatura void (sem erro 'void type not allowed')
        doNothing().when(deleteMenuUseCase).execute(id);

        // Act & Assert
        mockMvc.perform(delete("/api/v2/menus/{id}", id))
                .andExpect(status().isNoContent());

        verify(deleteMenuUseCase, times(1)).execute(id);
    }

}
