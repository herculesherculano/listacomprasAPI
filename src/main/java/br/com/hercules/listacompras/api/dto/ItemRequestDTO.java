package br.com.hercules.listacompras.api.dto;

import br.com.hercules.listacompras.api.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Positive(message = "A quantidade deve ser igual ou maior que 0")
    private Double quantidade;
    private Long categoriaId;
    private Status status;

    public ItemRequestDTO(String nome, Double quantidade, Status status){
        this.nome=nome;
        this.quantidade=quantidade;
        this.status=status;
    }

    public ItemRequestDTO(String nome){
        this.nome=nome;
    }
}
