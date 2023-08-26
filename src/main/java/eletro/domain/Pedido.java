package eletro.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Column(name = "checked", columnDefinition = "Boolean default false")
    private Boolean checked;

}
