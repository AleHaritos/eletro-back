package eletro.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "imagem")
@Getter
@Setter
public class Imagem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "url", nullable = false)
    private String url;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public Imagem() {

    }

    public Imagem(String url, Produto produto) {
        this.url = url;
        this.produto = produto;
    }

    public Imagem(Integer id, String url, Produto produto) {
        this.id = id;
        this.url = url;
        this.produto = produto;
    }


}
