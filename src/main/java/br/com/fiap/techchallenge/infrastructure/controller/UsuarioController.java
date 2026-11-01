package br.com.fiap.techchallenge.infrastructure.controller;

import br.com.fiap.techchallenge.infrastructure.controller.request.AtualizarSenhaRequest;
import br.com.fiap.techchallenge.infrastructure.controller.request.UsuarioRequest;
import br.com.fiap.techchallenge.infrastructure.controller.request.UsuarioUpdateRequest;
import br.com.fiap.techchallenge.infrastructure.controller.response.UsuarioResponse;
import br.com.fiap.techchallenge.usecase.port.input.UsuarioCommandUseCase;
import br.com.fiap.techchallenge.usecase.port.input.UsuarioQueryUseCase;
import br.com.fiap.techchallenge.usecase.assembler.UsuarioResponseAssembler;
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
@Tag(name = "Usuários", description = "API para gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioCommandUseCase usuarioCommandUseCase;
    private final UsuarioQueryUseCase usuarioQueryUseCase;
    private final UsuarioResponseAssembler usuarioResponseAssembler;

    public UsuarioController(
            UsuarioCommandUseCase usuarioCommandUseCase,
            UsuarioQueryUseCase usuarioQueryUseCase,
            UsuarioResponseAssembler usuarioResponseAssembler) {
        this.usuarioCommandUseCase = usuarioCommandUseCase;
        this.usuarioQueryUseCase = usuarioQueryUseCase;
        this.usuarioResponseAssembler = usuarioResponseAssembler;
    }

    @Operation(
            summary = "Cadastrar usuário",
            description = "Cria um novo usuário com senha protegida por BCrypt."
    )
    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso")
    @ApiResponse(responseCode = "422", description = "Dados inválidos ou regra de negócio violada (ex.: e-mail duplicado)")
    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioRequest request) {
        var usuario = usuarioCommandUseCase.create(request.nome(), request.email(), request.senha(), request.login(), request.enderecoId(), request.tipoUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseAssembler.toResponse(usuario));
    }

    @Operation(
            summary = "Atualizar dados do usuário",
            description = "Atualiza nome, e-mail, login, endereço e tipo do usuário. A senha não é alterada neste endpoint."
    )
    @ApiResponse(responseCode = "200", description = "Dados do usuário atualizados com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "422", description = "Dados inválidos ou conflito de regra de negócio")
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest request) {
        var usuario = usuarioCommandUseCase.update(id, request.nome(), request.email(), request.login(), request.enderecoId(), request.tipoUsuarioId());
        return ResponseEntity.ok(usuarioResponseAssembler.toResponse(usuario));
    }

    @Operation(
            summary = "Alterar senha do usuário",
            description = "Troca a senha do usuário em um endpoint exclusivo, aplicando hash BCrypt antes do armazenamento."
    )
    @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "422", description = "Nova senha inválida ou regra de negócio violada")
    @PutMapping("/usuarios/{id}/senha")
    public ResponseEntity<UsuarioResponse> updatePassword(@PathVariable Long id, @Valid @RequestBody AtualizarSenhaRequest request) {
        var usuario = usuarioCommandUseCase.changePassword(id, request.novaSenha());
        return ResponseEntity.ok(usuarioResponseAssembler.toResponse(usuario));
    }

    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> getById(@PathVariable Long id) {
        var usuario = usuarioQueryUseCase.findById(id);
        return ResponseEntity.ok(usuarioResponseAssembler.toResponse(usuario));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponse>> getAll() {
        var list = usuarioQueryUseCase.findAll().stream()
                .map(usuarioResponseAssembler::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/usuarios/nome/{nome}")
    public ResponseEntity<List<UsuarioResponse>> getByNomeContaining(@PathVariable String nome) {
        var list = usuarioQueryUseCase.findByNomeContaining(nome).stream()
                .map(usuarioResponseAssembler::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioCommandUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
