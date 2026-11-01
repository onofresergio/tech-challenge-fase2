package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.exception.EmailAlreadyExistsException;
import br.com.fiap.techchallenge.domain.exception.UsuarioNotFoundException;
import br.com.fiap.techchallenge.domain.exception.UsuarioValidationException;
import br.com.fiap.techchallenge.domain.model.Usuario;
import br.com.fiap.techchallenge.usecase.port.input.UsuarioUseCase;
import br.com.fiap.techchallenge.usecase.port.output.PasswordHasherPort;
import br.com.fiap.techchallenge.usecase.port.output.UsuarioRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;

public class UsuarioInteractor implements UsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordHasherPort passwordHasherPort;

    public UsuarioInteractor(UsuarioRepositoryPort usuarioRepositoryPort, PasswordHasherPort passwordHasherPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordHasherPort = passwordHasherPort;
    }

    @Override
    public Usuario findById(Long id) {
        return usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    @Override
    public Usuario create(String nome, String email, String senha, String login, Long enderecoId, Long tipoUsuarioId) {
        validarEmailUnico(email, null);
        var senhaHash = passwordHasherPort.hash(senha);
        var usuario = new Usuario(null, nome, email, senhaHash, login, LocalDateTime.now(), enderecoId, tipoUsuarioId);
        return usuarioRepositoryPort.save(usuario);
    }

    @Override
    public Usuario update(Long id, String nome, String email, String login, Long enderecoId, Long tipoUsuarioId) {
        var usuarioAtual = usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
        validarEmailUnico(email, id);

        var usuario = new Usuario(
                id,
                nome,
                email,
                usuarioAtual.senha(),
                login,
                LocalDateTime.now(),
                enderecoId,
                tipoUsuarioId
        );
        return usuarioRepositoryPort.save(usuario);
    }

    @Override
    public Usuario changePassword(Long id, String novaSenha) {
        var usuarioAtual = usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        var usuarioAtualizado = new Usuario(
                id,
                usuarioAtual.nome(),
                usuarioAtual.email(),
                passwordHasherPort.hash(novaSenha),
                usuarioAtual.login(),
                LocalDateTime.now(),
                usuarioAtual.enderecoId(),
                usuarioAtual.tipoUsuarioId()
        );

        return usuarioRepositoryPort.save(usuarioAtualizado);
    }

    private void validarEmailUnico(String email, Long usuarioIdAtual) {
        usuarioRepositoryPort.findByEmail(email)
                .ifPresent(usuarioExistente -> {
                    if (usuarioIdAtual == null || !usuarioExistente.id().equals(usuarioIdAtual)) {
                        throw new EmailAlreadyExistsException(email);
                    }
                });
    }

    @Override
    public void delete(Long id) {
        if (!usuarioRepositoryPort.existsById(id)) {
            throw new UsuarioNotFoundException(id);
        }
        usuarioRepositoryPort.deleteById(id);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepositoryPort.findAll();
    }

    @Override
    public List<Usuario> findByNomeContaining(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new UsuarioValidationException("O nome para busca não pode ser vazio.");
        }
        return usuarioRepositoryPort.findAll().stream()
                .filter(usuario -> usuario.nome() != null && usuario.nome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return usuarioRepositoryPort.existsById(id);
    }
}
