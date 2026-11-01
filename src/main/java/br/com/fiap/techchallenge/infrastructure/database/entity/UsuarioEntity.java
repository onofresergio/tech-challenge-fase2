package br.com.fiap.techchallenge.infrastructure.database.entity;

import br.com.fiap.techchallenge.domain.model.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "data_ultima_alteracao")
    private LocalDateTime dataUltimaAlteracao;

    @Column(name = "endereco_id", nullable = false)
    private Long enderecoId;

    @Column(name = "tipo_usuario_id", nullable = false)
    private Long tipoUsuarioId;

    public UsuarioEntity() {
    }

    public static UsuarioEntity fromDomain(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.id = usuario.id();
        entity.nome = usuario.nome();
        entity.email = usuario.email();
        entity.senha = usuario.senha();
        entity.login = usuario.login();
        entity.dataUltimaAlteracao = usuario.dataUltimaAlteracao();
        entity.enderecoId = usuario.enderecoId();
        entity.tipoUsuarioId = usuario.tipoUsuarioId();
        return entity;
    }

    public Usuario toDomain() {
        return new Usuario(
                this.id,
                this.nome,
                this.email,
                this.senha,
                this.login,
                this.dataUltimaAlteracao,
                this.enderecoId,
                this.tipoUsuarioId
        );
    }
}
