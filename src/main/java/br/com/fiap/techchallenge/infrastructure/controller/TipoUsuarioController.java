package br.com.fiap.techchallenge.infrastructure.controller;

import br.com.fiap.techchallenge.infrastructure.controller.request.CriarTipoUsuarioRequest;
import br.com.fiap.techchallenge.infrastructure.controller.request.TipoUsuarioRequest;
import br.com.fiap.techchallenge.infrastructure.controller.response.TipoUsuarioResponse;
import br.com.fiap.techchallenge.usecase.port.input.CriarTipoUsuarioUseCase;
import br.com.fiap.techchallenge.usecase.port.input.TipoUsuarioCommandUseCase;
import br.com.fiap.techchallenge.usecase.port.input.TipoUsuarioQueryUseCase;
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
@Tag(name = "Tipos de Usuário", description = "API para gerenciamento de tipos de usuário")
public class TipoUsuarioController {

    private final TipoUsuarioCommandUseCase tipoUsuarioCommandUseCase;
    private final TipoUsuarioQueryUseCase tipoUsuarioQueryUseCase;
    private final CriarTipoUsuarioUseCase criarTipoUsuarioUseCase;

    public TipoUsuarioController(
            TipoUsuarioCommandUseCase tipoUsuarioCommandUseCase,
            TipoUsuarioQueryUseCase tipoUsuarioQueryUseCase,
            CriarTipoUsuarioUseCase criarTipoUsuarioUseCase) {
        this.tipoUsuarioCommandUseCase = tipoUsuarioCommandUseCase;
        this.tipoUsuarioQueryUseCase = tipoUsuarioQueryUseCase;
        this.criarTipoUsuarioUseCase = criarTipoUsuarioUseCase;
    }

    @Operation(
            summary = "Cadastrar tipo de usuário",
            description = "Cria um novo tipo de usuário no sistema."
    )
    @ApiResponse(responseCode = "201", description = "Tipo de usuário cadastrado com sucesso")
    @ApiResponse(responseCode = "422", description = "Dados inválidos ou regra de negócio violada")
    @PostMapping("/tipo-usuario")
    public ResponseEntity<TipoUsuarioResponse> create(@Valid @RequestBody CriarTipoUsuarioRequest request) {
        var result = criarTipoUsuarioUseCase.create(request.nome());
        return ResponseEntity.status(HttpStatus.CREATED).body(new TipoUsuarioResponse(result.id(), result.nome()));
    }

    @Operation(
            summary = "Atualizar tipo de usuário",
            description = "Atualiza o nome do tipo de usuário informado pelo identificador."
    )
    @ApiResponse(responseCode = "200", description = "Tipo de usuário atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado")
    @ApiResponse(responseCode = "422", description = "Dados inválidos ou regra de negócio violada")
    @PutMapping("/tipo-usuario/{id}")
    public ResponseEntity<TipoUsuarioResponse> update(@PathVariable Long id, @Valid @RequestBody TipoUsuarioRequest request) {
        var result = tipoUsuarioCommandUseCase.update(id, request.nome());
        return ResponseEntity.ok(new TipoUsuarioResponse(result.id(), result.nome()));
    }

    @Operation(
            summary = "Buscar tipo de usuário por id",
            description = "Retorna um tipo de usuário específico com base no identificador informado."
    )
    @ApiResponse(responseCode = "200", description = "Tipo de usuário encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado")
    @GetMapping("/tipo-usuario/{id}")
    public ResponseEntity<TipoUsuarioResponse> getById(@PathVariable Long id) {
        var result = tipoUsuarioQueryUseCase.findById(id);
        return ResponseEntity.ok(new TipoUsuarioResponse(result.id(), result.nome()));
    }

    @Operation(
            summary = "Listar tipos de usuário",
            description = "Retorna a lista completa de tipos de usuário cadastrados."
    )
    @ApiResponse(responseCode = "200", description = "Lista de tipos de usuário retornada com sucesso")
    @GetMapping("/tipo-usuario")
    public ResponseEntity<List<TipoUsuarioResponse>> getAll() {
        var list = tipoUsuarioQueryUseCase.findAll().stream()
                .map(ut -> new TipoUsuarioResponse(ut.id(), ut.nome()))
                .toList();
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Excluir tipo de usuário",
            description = "Remove um tipo de usuário pelo identificador informado."
    )
    @ApiResponse(responseCode = "204", description = "Tipo de usuário excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado")
    @DeleteMapping("/tipo-usuario/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tipoUsuarioCommandUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
