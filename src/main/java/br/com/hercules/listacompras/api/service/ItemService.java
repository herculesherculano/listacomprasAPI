package br.com.hercules.listacompras.api.service;

import br.com.hercules.listacompras.api.model.Item;

import java.util.List;

public interface ItemService {

    public Item adicionarItem(Item novoItem);
    public List<Item> buscarTodosItens();
    public Item buscarItemPorId(Long id);
    public void deletarItem(Long id);
}
