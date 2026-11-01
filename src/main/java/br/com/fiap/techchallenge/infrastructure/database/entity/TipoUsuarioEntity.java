package br.com.fiap.techchallenge.infrastructure.database.entity;

import br.com.fiap.techchallenge.domain.model.TipoUsuario;
import jakarta.persistence.*;

@Entity
@Table(name = "tipo_usuario")
public class TipoUsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome; // Exemplo: "DONO_RESTAURANTE", "CLIENTE"

    public TipoUsuarioEntity() {}

    public static TipoUsuarioEntity fromDomain(TipoUsuario ut) {
        TipoUsuarioEntity entity = new TipoUsuarioEntity();
        entity.id = ut.id();
        entity.nome = ut.nome();
        return entity;
    }

    public TipoUsuario toDomain() {
        return new TipoUsuario(this.id, this.nome);
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
}
