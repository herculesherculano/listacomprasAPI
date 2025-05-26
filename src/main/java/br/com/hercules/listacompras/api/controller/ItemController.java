package br.com.hercules.listacompras.api.controller;

import br.com.hercules.listacompras.api.model.Item;
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
    public ResponseEntity<Item>adicionarItem(@RequestBody Item itemAdicionar){
        Item itemAdicionado=itemService.adicionarItem(itemAdicionar);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(itemAdicionado.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<Item>> buscarTodosItens(){
        var todosItens = itemService.buscarTodosItens();
        return ResponseEntity.ok(todosItens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> buscarItemPorId(@PathVariable Long id){
        var itemPorId = itemService.buscarItemPorId(id);
        return ResponseEntity.ok(itemPorId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Item> deletarItem(@PathVariable Long id){
        itemService.deletarItem(id);
        return ResponseEntity.noContent().build();

    }

}
