package eletro.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Table(name = "produto")
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", length = 35)
    private String nome;

    @Column(name = "preco")
    private Double preco;

    @Column(name = "preco_original", nullable = false)
    private Double precoOriginal;

    @Column(name = "off")
    private Integer off;

    @Column(name = "estoque", nullable = false)
    private Integer estoque;

    @Column(name = "descricao", length = 100, nullable = false)
    private String descricao;

    @Column(name = "categoria", length = 1) // not null
    private String categoria;

    @OneToMany(mappedBy = "produto", fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    private Set<Imagem> imagens = new HashSet<>();

    public Produto() {

    }
}
