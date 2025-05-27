package br.com.hercules.listacompras.api.dto;

import br.com.hercules.listacompras.api.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemResponseDTO {
    private Long id;
    private String nome;
    private Double quantidade;
    private CategoriaResponseDTO categoria;
    private Status status;


    public ItemResponseDTO(Long id, String nome, Double quantidade, Status status, CategoriaResponseDTO categoriaResponseDTO) {
        this.id=id;
        this.nome=nome;
        this.quantidade=quantidade;
        this.status=status;
        this.categoria=categoriaResponseDTO;
    }
}
