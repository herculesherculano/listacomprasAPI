package br.com.hercules.listacompras.api.service.impl;

import br.com.hercules.listacompras.api.dto.CategoriaRequestDTO;
import br.com.hercules.listacompras.api.dto.CategoriaResponseDTO;
import br.com.hercules.listacompras.api.dto.ItemRequestDTO;
import br.com.hercules.listacompras.api.dto.ItemResponseDTO;
import br.com.hercules.listacompras.api.exception.ResourceNotFoundException;
import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.model.Item;
import br.com.hercules.listacompras.api.model.Status;
import br.com.hercules.listacompras.api.repository.ItemRepository;
import br.com.hercules.listacompras.api.service.CategoriaService;
import br.com.hercules.listacompras.api.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {


    private final ItemRepository itemRepository;
    private final CategoriaService categoriaService;

    public ItemServiceImpl(ItemRepository itemRepository, CategoriaService categoriaService)  {
        this.itemRepository=itemRepository;
        this.categoriaService=categoriaService;

    }

    @Override
    public ItemResponseDTO adicionarItem(ItemRequestDTO novoItem) {

        String nomeCategoria = categoriaService.preverCategoria(novoItem.getNome());
        Categoria categoria = categoriaService.buscarCategoriaPorNome(nomeCategoria);

        Item item = new Item();
        item.setNome(novoItem.getNome());
        item.setQuantidade(novoItem.getQuantidade());
        item.setCategoria(categoria);
        item.setStatus(Status.PENDENTE);
        itemRepository.save(item);
        return new ItemResponseDTO(item.getId(), item.getNome(), item.getQuantidade(), item.getStatus(), new CategoriaResponseDTO(item.getCategoria().getId(), item.getCategoria().getNome()));
    }

    public List<ItemResponseDTO> buscarTodosItens(){
        List<Item> itens = itemRepository.findAll();
        return itens.stream()
                .map(item -> new ItemResponseDTO(
                        item.getId(),
                        item.getNome(),
                        item.getQuantidade(),
                        item.getStatus(),
                        new CategoriaResponseDTO(
                                item.getCategoria().getId(),
                                item.getCategoria().getNome()
                        )
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDTO buscarItemPorId(Long id) {
        var item = itemRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Item não encontrado"));
        return new ItemResponseDTO(
                item.getId(),
                item.getNome(),
                item.getQuantidade(),
                item.getStatus(),
                new CategoriaResponseDTO(
                        item.getCategoria().getId(),
                        item.getCategoria().getNome()
                )
        );
    }

    @Override
    public List<ItemResponseDTO> buscarPorCategoria(String nomeCategoria) {
        Categoria categoria = categoriaService.buscarCategoriaPorNome(nomeCategoria);
        List<Item> itens = itemRepository.findByCategoria(categoria);
        return itens.stream().map(
                item -> new ItemResponseDTO(
                        item.getId(),
                        item.getNome(),
                        item.getQuantidade(),
                        item.getStatus(),
                        new CategoriaResponseDTO(
                                item.getCategoria().getId(),
                                item.getCategoria().getNome()
                        )
                )).collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDTO> buscarPorStatus(Status status) {
        List<Item> itensPorStatus = itemRepository.findByStatus(status);
        return itensPorStatus.stream().map(
                item -> new ItemResponseDTO(
                        item.getId(),
                        item.getNome(),
                        item.getQuantidade(),
                        item.getStatus(),
                        new CategoriaResponseDTO(
                                item.getCategoria().getId(),
                                item.getCategoria().getNome()
                        )
                )).collect(Collectors.toList());
    }

    public List<ItemResponseDTO> buscarPorCategoriaStatus (String nomeCategoria, Status status){
        Categoria categoria = categoriaService.buscarCategoriaPorNome(nomeCategoria);
        List<Item> itensCategoriaStatus = itemRepository.findByCategoriaAndStatus(categoria, status);
        return itensCategoriaStatus.stream().map(
                item -> new ItemResponseDTO(
                        item.getId(),
                        item.getNome(),
                        item.getQuantidade(),
                        item.getStatus(),
                        new CategoriaResponseDTO(
                                item.getCategoria().getId(),
                                item.getCategoria().getNome()
                        )
                )).collect(Collectors.toList());
    }

    @Override
    public ItemResponseDTO alterarItem(Long id, ItemRequestDTO itemAlterado) {
        Item itemExistente = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));
        Categoria categoria = categoriaService.buscarCategoriaPorId(itemAlterado.getCategoriaId());
        itemExistente.setNome(itemAlterado.getNome());
        itemExistente.setQuantidade(itemAlterado.getQuantidade());
        itemExistente.setCategoria(categoria);
        itemExistente.setStatus(itemAlterado.getStatus());
        itemRepository.save(itemExistente);
        return new ItemResponseDTO(itemExistente.getId(),itemExistente.getNome(),itemExistente.getQuantidade(),itemExistente.getStatus(), new CategoriaResponseDTO(itemExistente.getCategoria().getId(), itemExistente.getCategoria().getNome()));
    }

    @Override
    public ItemResponseDTO alterarStatusItem(Long id, Status status) {
        Item item = itemRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Item não encontrado"));
        item.setStatus(status);
        itemRepository.save(item);
        return new ItemResponseDTO(item.getId(), item.getNome(), item.getQuantidade(), item.getStatus(), new CategoriaResponseDTO(item.getCategoria().getId(), item.getCategoria().getNome()));
    }

    @Override
    public ItemResponseDTO alterarCategoriaItem(Long id, String nomeCategoria) {
        Categoria categoria = categoriaService.buscarCategoriaPorNome(nomeCategoria);
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));
        item.setCategoria(categoria);
        itemRepository.save(item);
        return new ItemResponseDTO(item.getId(), item.getNome(), item.getQuantidade(), item.getStatus(), new CategoriaResponseDTO(item.getCategoria().getId(), item.getCategoria().getNome()));
    }

    @Override
    public void deletarItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item não encontrado");
        }
        itemRepository.deleteById(id);
    }
}
