package br.com.fiap.techchallenge.infrastructure.database.entity;

import br.com.fiap.techchallenge.domain.model.Menu;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "menu")
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "valor_prato", nullable = false)
    private BigDecimal valorPrato;

    @Column(name = "disponivel_restaurante", nullable = false)
    private boolean disponivelRestaurante;

    @Column(name = "foto_prato", nullable = false)
    private String fotoPrato;

    @Column(name = "restaurante_id", nullable = false)
    private Long restauranteId;

    public MenuEntity() {
    }

    public static MenuEntity fromDomain(Menu menu) {
        MenuEntity entity = new MenuEntity();
        entity.id = menu.id();
        entity.nome = menu.nome();
        entity.descricao = menu.descricao();
        entity.valorPrato = menu.valorPrato();
        entity.disponivelRestaurante = menu.disponivelRestaurante();
        entity.fotoPrato = menu.fotoPrato();
        entity.restauranteId = menu.restauranteId();
        return entity;
    }

    public Menu toDomain() {
        return new Menu(
                this.id,
                this.nome,
                this.descricao,
                this.valorPrato,
                this.disponivelRestaurante,
                this.fotoPrato,
                this.restauranteId
        );
    }
}
