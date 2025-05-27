package br.com.hercules.listacompras.api.controller;

import br.com.hercules.listacompras.api.dto.CategoriaRequestDTO;
import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService){
        this.categoriaService=categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaRequestDTO> cadastrarCategoria(@RequestBody CategoriaRequestDTO novaCategoria){
        var categoriaCadastrada = categoriaService.cadastrarCategoria(novaCategoria);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoriaCadastrada.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
