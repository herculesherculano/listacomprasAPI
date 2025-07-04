package br.com.hercules.listacompras.api.controller;

import br.com.hercules.listacompras.api.dto.CategoriaResponseDTO;
import br.com.hercules.listacompras.api.dto.ItemRequestDTO;
import br.com.hercules.listacompras.api.dto.ItemResponseDTO;
import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.model.Item;
import br.com.hercules.listacompras.api.model.Status;
import br.com.hercules.listacompras.api.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Adicionar item na lista", description = "Adiciona um novo item na lista e retorna os dados do item adicionado")
    @PostMapping
    public ResponseEntity<ItemResponseDTO>adicionarItem(@RequestBody  @Valid ItemRequestDTO itemAdicionar){
        var itemAdicionado = itemService.adicionarItem(itemAdicionar);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(itemAdicionado.getId())
                .toUri();
        return ResponseEntity.created(location).body(itemAdicionado);
    }

    @Operation(summary = "Listar todos os itens na lista", description = "Exibe uma lista com todos os itens adicionados a lista")
    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> buscarTodosItens(){
        var todosItens = itemService.buscarTodosItens();
        return ResponseEntity.ok(todosItens);
    }

    @Operation(summary = "Buscar item na lista pelo seu ID", description = "Retorna os detalhes de um item específico da lista")
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> buscarItemPorId(@PathVariable Long id){
        var itemPorId = itemService.buscarItemPorId(id);
        return ResponseEntity.ok(itemPorId);
    }

    @Operation(summary = "Buscar itens por Categoria", description = "Exibe uma lista com todos os itens da categoria escolhida")
    @GetMapping("/categoria/{nomeCategoria}")
    public ResponseEntity<List<ItemResponseDTO>> buscarPorCategoria(@PathVariable String nomeCategoria){
        var itensPorCategoria = itemService.buscarPorCategoria(nomeCategoria);
        return ResponseEntity.ok(itensPorCategoria);
    }

    @Operation(summary = "Buscar itens por Status", description = "Exibe uma lista com todos os itens do status buscado")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ItemResponseDTO>> buscarPorStatus(@PathVariable Status status){

        var itensPorStatus = itemService.buscarPorStatus(status);
        return ResponseEntity.ok(itensPorStatus);
    }

    @Operation(summary = "Buscar itens por status e categoria", description = "Exibe uma lista com todos os itens com categoria e status buscado")
    @GetMapping("/categoria/{nomeCategoria}/{status}")
    public ResponseEntity<List<ItemResponseDTO>> buscarPorCategoriaStatus(@PathVariable String nomeCategoria, @PathVariable Status status){
        var itensPorCategoriaStatus = itemService.buscarPorCategoriaStatus(nomeCategoria, status);
        return ResponseEntity.ok(itensPorCategoriaStatus);
    }

    @Operation(summary = "Alterar os dados de um item da lista", description = "Altera os dados de um item da lista através do seu ID")
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> alterarItem(@PathVariable Long id, @RequestBody ItemRequestDTO itemParaAtualizar){
        var itemAtualizado = itemService.alterarItem(id, itemParaAtualizar);
        return ResponseEntity.ok(itemAtualizado);
    }

    @Operation(summary = "Alterar o status de um item da lista", description = "Altera o status de um item da lista através do seu ID")
    @PutMapping("/{id}/status")
    public ResponseEntity<ItemResponseDTO> alterarStatusItem(@PathVariable Long id, @RequestParam Status status){
        var statusAtualizado = itemService.alterarStatusItem(id, status);
        return ResponseEntity.ok(statusAtualizado);
    }

    @Operation(summary = "Alterar categoria de um item da lista", description = "Altera a categoria do item da lista através do seu ID")
    @PutMapping("/{id}/categoria")
    public ResponseEntity<ItemResponseDTO> alterarCategoriaItem(@PathVariable Long id, @RequestBody String nomeCategoria){
        var categoriaAtualizada = itemService.alterarCategoriaItem(id, nomeCategoria);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    @Operation(summary = "Deletar um item da lista", description = "Deleta um item da lista através do seu ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarItem(@PathVariable Long id){
        itemService.deletarItem(id);
        return ResponseEntity.noContent().build();

    }

}
