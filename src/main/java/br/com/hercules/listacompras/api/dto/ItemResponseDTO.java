package br.com.hercules.listacompras.api.dto;

import br.com.hercules.listacompras.api.model.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemResponseDTO {
    private Long id;
    private String nome;
    private Double quantidade;
    private CategoriaResponseDTO categoria;
    private Status status;
}
