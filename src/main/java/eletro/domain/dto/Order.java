package eletro.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {

    private Double preco;
    private String moeda;
    private String metodo;
    private String intent;
    private String descricao;

}
