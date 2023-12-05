package eletro.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties

public class FreteDTO {

    private Long id;
    private String name;
    private String price;
    private String delivery_time;

}
