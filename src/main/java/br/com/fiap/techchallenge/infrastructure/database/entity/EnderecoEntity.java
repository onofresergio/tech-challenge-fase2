package br.com.fiap.techchallenge.infrastructure.database.entity;

import br.com.fiap.techchallenge.domain.model.Endereco;
import jakarta.persistence.*;

@Entity
@Table(name = "endereco")
public class EnderecoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rua", nullable = false)
    private String rua;

    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "cidade", nullable = false)
    private String cidade;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "cep", nullable = false)
    private String cep;

    public EnderecoEntity() {
    }

    public static EnderecoEntity fromDomain(Endereco endereco) {
        EnderecoEntity entity = new EnderecoEntity();
        entity.id = endereco.id();
        entity.rua = endereco.rua();
        entity.numero = endereco.numero();
        entity.cidade = endereco.cidade();
        entity.estado = endereco.estado();
        entity.cep = endereco.cep();
        return entity;
    }

    public Endereco toDomain() {
        return new Endereco(this.id, this.rua, this.numero, this.cidade, this.estado, this.cep);
    }
}
