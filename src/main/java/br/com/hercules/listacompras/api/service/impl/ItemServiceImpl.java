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
    public Item adicionarItem(ItemRequestDTO novoItem) {

        Categoria categoria = categoriaRepository.findById(novoItem.getCategoriaId()).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Item item = new Item();
        item.setNome(novoItem.getNome());
        item.setQuantidade(novoItem.getQuantidade());
        item.setCategoria(categoria);
        item.setStatus(novoItem.getStatus());
        return itemRepository.save(item);
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
    public Item buscarItemPorId(Long id) {
       return itemRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Item não encontrado"));
    }

    @Override
    public void deletarItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item não encontrado");
        }
        itemRepository.deleteById(id);
    }

    @Override
    public List<Item> buscarPorCategoria(Categoria categoria) {
        return itemRepository.findByCategoria(categoria);
    }

    @Override
    public List<Item> buscarPorStatus(Status status) {
        return itemRepository.findByStatus(status);
    }
}
