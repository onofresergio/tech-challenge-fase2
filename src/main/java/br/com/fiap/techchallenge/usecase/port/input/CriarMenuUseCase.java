package br.com.fiap.techchallenge.usecase.port.input;

import br.com.fiap.techchallenge.domain.model.Menu;

import java.math.BigDecimal;

public interface CriarMenuUseCase {
    Menu execute(String nome, String descricao, BigDecimal valorPrato, boolean disponivelRestaurante, String fotoPrato, Long restauranteId);
}
