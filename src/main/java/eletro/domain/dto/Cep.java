package eletro.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cep {
    private String bairro;
    private String logradouro;
    private String localidade;
    private String uf;
    private String cep;
    private Boolean erro;
}
