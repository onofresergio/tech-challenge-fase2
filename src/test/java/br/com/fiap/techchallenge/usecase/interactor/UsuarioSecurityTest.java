package br.com.fiap.techchallenge.usecase.interactor;

import br.com.fiap.techchallenge.domain.model.Usuario;
import br.com.fiap.techchallenge.infrastructure.database.adapter.UsuarioRepositoryAdapter;
import br.com.fiap.techchallenge.infrastructure.database.entity.UsuarioEntity;
import br.com.fiap.techchallenge.infrastructure.database.jpa.UsuarioJpaRepository;
import br.com.fiap.techchallenge.usecase.port.output.PasswordHasherPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioSecurityTest {

    @Mock
    private UsuarioJpaRepository repository;

    @Mock
    private PasswordHasherPort passwordHasherPort;

    @Test
    void createShouldHashPasswordBeforePersisting() {
        var expectedHash = "$2a$10$hashedPassword";
        var usuarioRepositoryPort = new FakeUsuarioRepositoryPort();
        var usuarioInteractor = new UsuarioInteractor(usuarioRepositoryPort, passwordHasherPort);

        when(passwordHasherPort.hash("senha123")).thenReturn(expectedHash);

        var usuario = usuarioInteractor.create(
                "Maria",
                "maria@email.com",
                "senha123",
                "maria",
                1L,
                2L
        );

        assertEquals(expectedHash, usuario.senha());
        assertEquals(expectedHash, usuarioRepositoryPort.saved.senha());
        verify(passwordHasherPort).hash("senha123");
    }

    @Test
    void repositoryAdapterShouldPersistProvidedPasswordWithoutRehashing() {
        var repositoryAdapter = new UsuarioRepositoryAdapter(repository, passwordHasherPort);

        when(repository.save(any(UsuarioEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var usuario = repositoryAdapter.save(new Usuario(
                null,
                "Maria",
                "maria@email.com",
                "senha123",
                "maria",
                LocalDateTime.now(),
                1L,
                2L
        ));

        assertEquals("senha123", usuario.senha());
        verify(passwordHasherPort, org.mockito.Mockito.never()).hash(any());
    }

    @Test
    void createShouldRejectDuplicateEmail() {
        var usuarioRepositoryPort = new FakeUsuarioRepositoryPort();
        var usuarioInteractor = new UsuarioInteractor(usuarioRepositoryPort, passwordHasherPort);

        usuarioRepositoryPort.existingEmail = "maria@email.com";

        var exception = assertThrows(RuntimeException.class, () -> usuarioInteractor.create(
                "Maria",
                "maria@email.com",
                "senha123",
                "maria",
                1L,
                2L
        ));

        assertEquals("O e-mail 'maria@email.com' já está cadastrado no sistema.", exception.getMessage());
    }

    private static class FakeUsuarioRepositoryPort implements br.com.fiap.techchallenge.usecase.port.output.UsuarioRepositoryPort {
        private Usuario saved;
        private String existingEmail;

        @Override
        public Usuario save(Usuario usuario) {
            this.saved = usuario;
            return usuario;
        }

        @Override
        public java.util.List<Usuario> findAll() {
            return java.util.List.of();
        }

        @Override
        public java.util.Optional<Usuario> findById(Long id) {
            return java.util.Optional.empty();
        }

        @Override
        public java.util.Optional<Usuario> findByEmail(String email) {
            if (email.equals(existingEmail)) {
                return java.util.Optional.of(new Usuario(
                        99L,
                        "Maria Existente",
                        email,
                        "$2a$10$existing",
                        "maria-existente",
                        LocalDateTime.now(),
                        1L,
                        2L
                ));
            }
            return java.util.Optional.empty();
        }

        @Override
        public void deleteById(Long id) {
        }

        @Override
        public boolean existsById(Long id) {
            return false;
        }
    }
}
