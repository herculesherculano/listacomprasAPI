package br.com.hercules.listacompras.api.controller;

import br.com.hercules.listacompras.api.dto.CategoriaRequestDTO;
import br.com.hercules.listacompras.api.dto.CategoriaResponseDTO;
import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService){
        this.categoriaService=categoriaService;
    }

    @Operation(summary = "Adicionar categoria", description = "Adiciona uma nova categoria")
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> cadastrarCategoria(@RequestBody CategoriaRequestDTO novaCategoria){
        var categoriaCadastrada = categoriaService.cadastrarCategoria(novaCategoria);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoriaCadastrada.getId())
                .toUri();
        return ResponseEntity.created(location).body(categoriaCadastrada);
    }
    @Operation(summary = "Buscar Categorias", description = "Exibe uma lista com todas as categorias cadastradas")
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> buscarTodasCategorias(){
        var todasCategorias = categoriaService.buscarTodasCategorias();
        return ResponseEntity.ok(todasCategorias);
    }

}
