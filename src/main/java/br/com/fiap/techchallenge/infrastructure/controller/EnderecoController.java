package br.com.fiap.techchallenge.infrastructure.controller;

import br.com.fiap.techchallenge.infrastructure.controller.request.EnderecoRequest;
import br.com.fiap.techchallenge.infrastructure.controller.response.EnderecoResponse;
import br.com.fiap.techchallenge.usecase.port.input.EnderecoCommandUseCase;
import br.com.fiap.techchallenge.usecase.port.input.EnderecoQueryUseCase;
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
@Tag(name = "Endereços", description = "API para gerenciamento de endereços")
public class EnderecoController {

    private final EnderecoCommandUseCase enderecoCommandUseCase;
    private final EnderecoQueryUseCase enderecoQueryUseCase;

    public EnderecoController(
            EnderecoCommandUseCase enderecoCommandUseCase,
            EnderecoQueryUseCase enderecoQueryUseCase) {
        this.enderecoCommandUseCase = enderecoCommandUseCase;
        this.enderecoQueryUseCase = enderecoQueryUseCase;
    }

    @Operation(
            summary = "Cadastrar endereço",
            description = "Cria um novo endereço conforme os dados informados."
    )
    @ApiResponse(responseCode = "201", description = "Endereço cadastrado com sucesso")
    @ApiResponse(responseCode = "422", description = "Dados inválidos ou regra de negócio violada")
    @PostMapping("/enderecos")
    public ResponseEntity<EnderecoResponse> create(@Valid @RequestBody EnderecoRequest request) {
        var endereco = enderecoCommandUseCase.create(request.rua(), request.numero(), request.cidade(), request.estado(), request.cep());
        var response = new EnderecoResponse(endereco.id(), endereco.rua(), endereco.numero(), endereco.cidade(), endereco.estado(), endereco.cep());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Atualizar endereço",
            description = "Atualiza os dados do endereço informado pelo identificador."
    )
    @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    @ApiResponse(responseCode = "422", description = "Dados inválidos ou regra de negócio violada")
    @PutMapping("/enderecos/{id}")
    public ResponseEntity<EnderecoResponse> update(@PathVariable Long id, @Valid @RequestBody EnderecoRequest request) {
        var endereco = enderecoCommandUseCase.update(id, request.rua(), request.numero(), request.cidade(), request.estado(), request.cep());
        var response = new EnderecoResponse(endereco.id(), endereco.rua(), endereco.numero(), endereco.cidade(), endereco.estado(), endereco.cep());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Buscar endereço por id",
            description = "Retorna um endereço específico com base no identificador informado."
    )
    @ApiResponse(responseCode = "200", description = "Endereço encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    @GetMapping("/enderecos/{id}")
    public ResponseEntity<EnderecoResponse> getById(@PathVariable Long id) {
        var endereco = enderecoQueryUseCase.findById(id);
        var response = new EnderecoResponse(endereco.id(), endereco.rua(), endereco.numero(), endereco.cidade(), endereco.estado(), endereco.cep());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Listar endereços",
            description = "Retorna a lista completa de endereços cadastrados."
    )
    @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso")
    @GetMapping("/enderecos")
    public ResponseEntity<List<EnderecoResponse>> getAll() {
        var list = enderecoQueryUseCase.findAll().stream()
                .map(endereco -> new EnderecoResponse(endereco.id(), endereco.rua(), endereco.numero(), endereco.cidade(), endereco.estado(), endereco.cep()))
                .toList();
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Excluir endereço",
            description = "Remove um endereço do sistema pelo identificador informado."
    )
    @ApiResponse(responseCode = "204", description = "Endereço excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    @DeleteMapping("/enderecos/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enderecoCommandUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
