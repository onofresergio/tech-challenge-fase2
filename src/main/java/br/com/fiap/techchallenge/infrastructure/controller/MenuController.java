package br.com.fiap.techchallenge.infrastructure.controller;

import br.com.fiap.techchallenge.infrastructure.controller.request.AtualizarMenuRequest;
import br.com.fiap.techchallenge.infrastructure.controller.request.CriarMenuRequest;
import br.com.fiap.techchallenge.infrastructure.controller.response.MenuResponse;
import br.com.fiap.techchallenge.usecase.port.input.CriarMenuUseCase;
import br.com.fiap.techchallenge.usecase.port.input.DeleteMenuUseCase;
import br.com.fiap.techchallenge.usecase.port.input.MenuQueryUseCase;
import br.com.fiap.techchallenge.usecase.port.input.UpdateMenuUseCase;
import br.com.fiap.techchallenge.usecase.assembler.MenuResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
@Tag(name = "Menus", description = "API para gerenciamento de menu")
public class MenuController {

    private final CriarMenuUseCase criarMenuUseCase;
    private final MenuQueryUseCase menuQueryUseCase;
    private final UpdateMenuUseCase updateMenuUseCase;
    private final DeleteMenuUseCase deleteMenuUseCase;
    private final MenuResponseAssembler menuResponseAssembler;

    public MenuController(
            CriarMenuUseCase criarMenuUseCase,
            MenuQueryUseCase menuQueryUseCase,
            UpdateMenuUseCase updateMenuUseCase,
            DeleteMenuUseCase deleteMenuUseCase,
            MenuResponseAssembler menuResponseAssembler) {
        this.criarMenuUseCase = criarMenuUseCase;
        this.menuQueryUseCase = menuQueryUseCase;
        this.updateMenuUseCase = updateMenuUseCase;
        this.deleteMenuUseCase = deleteMenuUseCase;
        this.menuResponseAssembler = menuResponseAssembler;
    }

    @Operation(
            summary = "Cadastrar menu",
            description = "Cria um novo item de menu associado a um restaurante."
    )
    @ApiResponse(responseCode = "201", description = "Menu cadastrado com sucesso")
    @ApiResponse(responseCode = "422", description = "Dados inválidos ou regra de negócio violada")
    @PostMapping("/menus")
    public ResponseEntity<MenuResponse> create(@Valid @RequestBody CriarMenuRequest request) {
        var menu = criarMenuUseCase.execute(
                request.nome(),
                request.descricao(),
                request.valorPrato(),
                request.disponivelRestaurante(),
                request.fotoPrato(),
                request.restauranteId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(menuResponseAssembler.toResponse(menu));
    }

    @Operation(
            summary = "Listar menus",
            description = "Retorna todos os menus cadastrados, incluindo o restaurante relacionado."
    )
    @ApiResponse(responseCode = "200", description = "Lista de menus retornada com sucesso")
    @GetMapping("/menus")
    public ResponseEntity<List<MenuResponse>> getAll() {
        var response = menuQueryUseCase.listar().stream()
                .map(menuResponseAssembler::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Buscar menu por id",
            description = "Retorna um menu específico com os dados do restaurante e relacionamentos associados."
    )
    @ApiResponse(responseCode = "200", description = "Menu encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Menu não encontrado")
    @GetMapping("/menus/{id}")
    public ResponseEntity<MenuResponse> getById(@PathVariable Long id) {
        var menu = menuQueryUseCase.buscarPorId(id);
        return ResponseEntity.ok(menuResponseAssembler.toResponse(menu));
    }

    @Operation(
            summary = "Atualizar menu",
            description = "Atualiza os dados do menu informado."
    )
    @ApiResponse(responseCode = "200", description = "Menu atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Menu não encontrado")
    @ApiResponse(responseCode = "422", description = "Dados inválidos ou regra de negócio violada")
    @PutMapping("/menus/{id}")
    public ResponseEntity<MenuResponse> update(@PathVariable Long id, @Valid @RequestBody AtualizarMenuRequest request) {
        var menu = updateMenuUseCase.execute(
                id,
                request.nome(),
                request.descricao(),
                request.valorPrato(),
                request.disponivelRestaurante(),
                request.fotoPrato()
        );

        return ResponseEntity.ok(menuResponseAssembler.toResponse(menu));
    }

    @Operation(
            summary = "Excluir menu",
            description = "Remove um menu do sistema pelo identificador informado."
    )
    @ApiResponse(responseCode = "204", description = "Menu excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Menu não encontrado")
    @DeleteMapping("/menus/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteMenuUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
