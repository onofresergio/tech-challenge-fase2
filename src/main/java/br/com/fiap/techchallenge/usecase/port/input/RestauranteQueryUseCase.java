package br.com.fiap.techchallenge.usecase.port.input;

import br.com.fiap.techchallenge.domain.model.Restaurante;

import java.util.List;

public interface RestauranteQueryUseCase {
    List<Restaurante> listar();
    Restaurante buscarPorId(Long id);
}
