package br.com.hercules.listacompras.api.service.impl;

import br.com.hercules.listacompras.api.exception.ResourceNotFoundException;
import br.com.hercules.listacompras.api.model.Item;
import br.com.hercules.listacompras.api.repository.ItemRepository;
import br.com.hercules.listacompras.api.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {


    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository){
        this.itemRepository=itemRepository;

    }

    @Override
    public Item adicionarItem(Item novoItem) {
        return itemRepository.save(novoItem);
    }

    public List<Item> buscarTodosItens(){
        return itemRepository.findAll();
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
}
