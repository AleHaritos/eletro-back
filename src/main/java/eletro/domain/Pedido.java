package eletro.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Data
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "email_usuario")
    private String emailUsuario;

    @Column(name = "produtos")
    private String produtos;

    @Column(name = "cep")
    private String cep;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "logradouro")
    private String logradouro;

    @Column(name = "data")
    private Date data;

    @Column(name = "valor_total")
    private Double valorTotal;

}
