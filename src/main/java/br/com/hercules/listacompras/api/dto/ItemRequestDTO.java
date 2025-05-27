package br.com.hercules.listacompras.api.dto;

import br.com.hercules.listacompras.api.model.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequestDTO {

    private String nome;
    private Double quantidade;
    private Long categoriaId;
    private Status status;
}
