package br.com.hercules.listacompras.api.service.impl;

import br.com.hercules.listacompras.api.dto.CategoriaResponseDTO;
import br.com.hercules.listacompras.api.dto.ItemRequestDTO;
import br.com.hercules.listacompras.api.dto.ItemResponseDTO;
import br.com.hercules.listacompras.api.exception.ResourceNotFoundException;
import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.model.Item;
import br.com.hercules.listacompras.api.model.Status;
import br.com.hercules.listacompras.api.repository.CategoriaRepository;
import br.com.hercules.listacompras.api.repository.ItemRepository;
import br.com.hercules.listacompras.api.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {


    private final ItemRepository itemRepository;
    private final CategoriaRepository categoriaRepository;

    public ItemServiceImpl(ItemRepository itemRepository, CategoriaRepository categoriaRepository) {
        this.itemRepository=itemRepository;
        this.categoriaRepository=categoriaRepository;

    }

    @Override
    public ItemResponseDTO adicionarItem(ItemRequestDTO novoItem) {

        Categoria categoria = categoriaRepository.findById(novoItem.getCategoriaId()).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Item item = new Item();
        item.setNome(novoItem.getNome());
        item.setQuantidade(novoItem.getQuantidade());
        item.setCategoria(categoria);
        item.setStatus(novoItem.getStatus());
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
        Categoria categoria = categoriaRepository.findByNomeIgnoreCase(nomeCategoria).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
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

    @Override
    public ItemResponseDTO alterarItem(Long id, ItemRequestDTO itemAlterado) {
        Item itemExistente = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item não encontrado"));
        Categoria categoria = categoriaRepository.findById(itemAlterado.getCategoriaId()).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
        itemExistente.setNome(itemAlterado.getNome());
        itemExistente.setQuantidade(itemAlterado.getQuantidade());
        itemExistente.setCategoria(categoria);
        itemExistente.setStatus(itemAlterado.getStatus());
        itemRepository.save(itemExistente);
        return new ItemResponseDTO(itemExistente.getId(),itemExistente.getNome(),itemExistente.getQuantidade(),itemExistente.getStatus(), new CategoriaResponseDTO(itemExistente.getCategoria().getId(), itemExistente.getCategoria().getNome()));
    }


    @Override
    public void deletarItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item não encontrado");
        }
        itemRepository.deleteById(id);
    }
}
