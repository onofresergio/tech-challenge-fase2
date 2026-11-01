package br.com.fiap.techchallenge.infrastructure.controller;

import br.com.fiap.techchallenge.infrastructure.controller.request.AtualizarRestauranteRequest;
import br.com.fiap.techchallenge.infrastructure.controller.request.CriarRestauranteRequest;
import br.com.fiap.techchallenge.infrastructure.controller.response.RestauranteResponse;
import br.com.fiap.techchallenge.usecase.port.input.CriarRestauranteUseCase;
import br.com.fiap.techchallenge.usecase.port.input.DeleteRestauranteUseCase;
import br.com.fiap.techchallenge.usecase.port.input.RestauranteQueryUseCase;
import br.com.fiap.techchallenge.usecase.port.input.UpdateRestauranteUseCase;
import br.com.fiap.techchallenge.usecase.assembler.RestauranteResponseAssembler;
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
@Tag(name = "Restaurantes", description = "API para gerenciamento de restaurantes")
public class RestauranteController {

    private final CriarRestauranteUseCase criarRestauranteUseCase;
    private final RestauranteQueryUseCase restauranteQueryUseCase;
    private final UpdateRestauranteUseCase updateRestauranteUseCase;
    private final DeleteRestauranteUseCase deleteRestauranteUseCase;
    private final RestauranteResponseAssembler restauranteResponseAssembler;

    public RestauranteController(
            CriarRestauranteUseCase criarRestauranteUseCase,
            RestauranteQueryUseCase restauranteQueryUseCase,
            UpdateRestauranteUseCase updateRestauranteUseCase,
            DeleteRestauranteUseCase deleteRestauranteUseCase,
            RestauranteResponseAssembler restauranteResponseAssembler) {
        this.criarRestauranteUseCase = criarRestauranteUseCase;
        this.restauranteQueryUseCase = restauranteQueryUseCase;
        this.updateRestauranteUseCase = updateRestauranteUseCase;
        this.deleteRestauranteUseCase = deleteRestauranteUseCase;
        this.restauranteResponseAssembler = restauranteResponseAssembler;
    }

    @Operation(
            summary = "Cadastrar restaurante",
            description = "Cria um novo restaurante com endereço, tipo de cozinha e tipo de usuário vinculados."
    )
    @ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso")
    @ApiResponse(responseCode = "422", description = "Dados inválidos ou regra de negócio violada")
    @PostMapping("/restaurantes")
    public ResponseEntity<RestauranteResponse> create(@Valid @RequestBody CriarRestauranteRequest request) {
        var restaurant = criarRestauranteUseCase.execute(
                request.nome(),
                request.enderecoId(),
                request.tipoCozinhaId(),
                request.horarioFuncionamento(),
                request.tipoUsuarioId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(restauranteResponseAssembler.toResponse(restaurant));
    }

    @Operation(
            summary = "Listar restaurantes",
            description = "Retorna todos os restaurantes cadastrados com seus relacionamentos expandidos."
    )
    @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    @GetMapping("/restaurantes")
    public ResponseEntity<List<RestauranteResponse>> getAll() {
        var response = restauranteQueryUseCase.listar().stream()
                .map(restauranteResponseAssembler::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Buscar restaurante por id",
            description = "Retorna um restaurante específico com todos os dados de relacionamento preenchidos."
    )
    @ApiResponse(responseCode = "200", description = "Restaurante encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    @GetMapping("/restaurantes/{id}")
    public ResponseEntity<RestauranteResponse> getById(@PathVariable Long id) {
        var restaurant = restauranteQueryUseCase.buscarPorId(id);
        return ResponseEntity.ok(restauranteResponseAssembler.toResponse(restaurant));
    }

    @Operation(
            summary = "Atualizar restaurante",
            description = "Atualiza os dados principais do restaurante, preservando seus relacionamentos e horários."
    )
    @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    @ApiResponse(responseCode = "422", description = "Dados inválidos ou regra de negócio violada")
    @PutMapping("/restaurantes/{id}")
    public ResponseEntity<RestauranteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarRestauranteRequest request) {
        var restaurant = updateRestauranteUseCase.execute(
                id,
                request.nome(),
                request.enderecoId(),
                request.tipoCozinhaId(),
                request.horarioFuncionamento(),
                request.tipoUsuarioId()
        );

        return ResponseEntity.ok(restauranteResponseAssembler.toResponse(restaurant));
    }

    @Operation(
            summary = "Excluir restaurante",
            description = "Remove um restaurante do sistema pelo identificador informado."
    )
    @ApiResponse(responseCode = "204", description = "Restaurante excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    @DeleteMapping("/restaurantes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteRestauranteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
