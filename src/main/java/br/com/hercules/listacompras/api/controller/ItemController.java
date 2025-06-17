package br.com.hercules.listacompras.api.controller;

import br.com.hercules.listacompras.api.dto.CategoriaResponseDTO;
import br.com.hercules.listacompras.api.dto.ItemRequestDTO;
import br.com.hercules.listacompras.api.dto.ItemResponseDTO;
import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.model.Item;
import br.com.hercules.listacompras.api.model.Status;
import br.com.hercules.listacompras.api.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemResponseDTO>adicionarItem(@RequestBody  @Valid ItemRequestDTO itemAdicionar){
        var itemAdicionado = itemService.adicionarItem(itemAdicionar);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(itemAdicionado.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> buscarTodosItens(){
        var todosItens = itemService.buscarTodosItens();
        return ResponseEntity.ok(todosItens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> buscarItemPorId(@PathVariable Long id){
        var itemPorId = itemService.buscarItemPorId(id);
        return ResponseEntity.ok(itemPorId);
    }

    @GetMapping("/categoria/{nomeCategoria}")
    public ResponseEntity<List<ItemResponseDTO>> buscarPorCategoria(@PathVariable String nomeCategoria){
        var itensPorCategoria = itemService.buscarPorCategoria(nomeCategoria);
        return ResponseEntity.ok(itensPorCategoria);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ItemResponseDTO>> buscarPorStatus(@PathVariable Status status){

        var itensPorStatus = itemService.buscarPorStatus(status);
        return ResponseEntity.ok(itensPorStatus);
    }

    @GetMapping("/categoria/{nomeCategoria}/{status}")
    public ResponseEntity<List<ItemResponseDTO>> buscarPorCategoriaStatus(@PathVariable String nomeCategoria, @PathVariable Status status){
        var itensPorCategoriaStatus = itemService.buscarPorCategoriaStatus(nomeCategoria, status);
        return ResponseEntity.ok(itensPorCategoriaStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> alterarItem(@PathVariable Long id, @RequestBody ItemRequestDTO itemParaAtualizar){
        var itemAtualizado = itemService.alterarItem(id, itemParaAtualizar);
        return ResponseEntity.ok(itemAtualizado);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ItemResponseDTO> alterarStatusItem(@PathVariable Long id, @RequestParam Status status){
        var statusAtualizado = itemService.alterarStatusItem(id, status);
        return ResponseEntity.ok(statusAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> deletarItem(@PathVariable Long id){
        itemService.deletarItem(id);
        return ResponseEntity.noContent().build();

    }

}
