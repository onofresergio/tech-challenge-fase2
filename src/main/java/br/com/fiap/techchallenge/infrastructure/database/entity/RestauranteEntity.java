package br.com.fiap.techchallenge.infrastructure.database.entity;

import br.com.fiap.techchallenge.domain.model.Restaurante;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "restaurante")
public class RestauranteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "endereco_id", nullable = false)
    private Long enderecoId;

    @Column(name = "tipo_cozinha_id", nullable = false)
    private Long tipoCozinhaId;

    @Column(name = "horario_funcionamento", nullable = false)
    private LocalDateTime horarioFuncionamento;

    @Column(name = "data_ultima_alteracao")
    private LocalDateTime lastModifiedDate;

    @Column(name = "tipo_usuario_id", nullable = false)
    private Long tipoUsuarioId;

    public RestauranteEntity() {}

    // Converte o modelo de Domínio puro para a Entidade do JPA
    public static RestauranteEntity fromDomain(Restaurante restaurante) {
        RestauranteEntity entity = new RestauranteEntity();
        entity.id = restaurante.id();
        entity.nome = restaurante.nome();
        entity.enderecoId = restaurante.enderecoId();
        entity.tipoCozinhaId = restaurante.tipoCozinhaId();
        entity.horarioFuncionamento = restaurante.horarioFuncionamento();
        //entity.lastModifiedDate = restaurante.dataUltimaAlteracao();
        entity.tipoUsuarioId = restaurante.tipoUsuarioId();
        return entity;
    }

    // Converte a Entidade do JPA de volta para o Domínio puro
    public Restaurante toDomain() {
        return new Restaurante(
                this.id,
                this.nome,
                this.enderecoId,
                this.tipoCozinhaId,
                this.horarioFuncionamento,
                //this.lastModifiedDate,
                this.tipoUsuarioId
        );
    }

}
