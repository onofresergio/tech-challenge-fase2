package br.com.fiap.techchallenge.usecase.port.input;

import br.com.fiap.techchallenge.domain.model.Usuario;

public interface AtualizarSenhaUsuarioUseCase {
    Usuario changePassword(Long id, String novaSenha);
}
