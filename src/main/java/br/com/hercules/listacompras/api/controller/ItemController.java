package br.com.hercules.listacompras.api.controller;

import br.com.hercules.listacompras.api.dto.ItemRequestDTO;
import br.com.hercules.listacompras.api.dto.ItemResponseDTO;
import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.model.Item;
import br.com.hercules.listacompras.api.model.Status;
import br.com.hercules.listacompras.api.service.ItemService;
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
    public ResponseEntity<Item>adicionarItem(@RequestBody ItemRequestDTO itemAdicionar){
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
    public ResponseEntity<Item> buscarItemPorId(@PathVariable Long id){
        var itemPorId = itemService.buscarItemPorId(id);
        return ResponseEntity.ok(itemPorId);
    }

    @GetMapping("/{categoria}")
    public ResponseEntity<List<Item>> buscarPorCategoria(@PathVariable Categoria categoria){
        var itensPorCategoria = itemService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(itensPorCategoria);
    }

    @GetMapping("/{status}")
    public ResponseEntity<List<Item>> buscarPorStatus(@PathVariable Status status){
        var itensPorStatus = itemService.buscarPorStatus(status);
        return ResponseEntity.ok(itensPorStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Item> deletarItem(@PathVariable Long id){
        itemService.deletarItem(id);
        return ResponseEntity.noContent().build();

    }

}
