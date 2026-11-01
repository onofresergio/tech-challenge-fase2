package br.com.fiap.techchallenge.infrastructure.database.entity;

import br.com.fiap.techchallenge.domain.model.TipoCozinha;
import jakarta.persistence.*;

@Entity
@Table(name = "tipo_cozinha")
public class TipoCozinhaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    public TipoCozinhaEntity() {}

    public static TipoCozinhaEntity fromDomain(TipoCozinha tipoCozinha) {
        TipoCozinhaEntity entity = new TipoCozinhaEntity();
        entity.id = tipoCozinha.id();
        entity.nome = tipoCozinha.nome();
        return entity;
    }

    public TipoCozinha toDomain() {
        return new TipoCozinha(this.id, this.nome);
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
}
