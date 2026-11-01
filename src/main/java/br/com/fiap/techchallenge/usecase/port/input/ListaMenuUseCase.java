package br.com.fiap.techchallenge.usecase.port.input;

import br.com.fiap.techchallenge.domain.model.Menu;

import java.util.List;

public interface ListaMenuUseCase {
    List<Menu> listar();
    Menu buscarPorId(Long id);
}
