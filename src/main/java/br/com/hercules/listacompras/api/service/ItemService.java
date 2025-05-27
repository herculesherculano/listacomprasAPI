package br.com.hercules.listacompras.api.service;

import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.model.Item;
import br.com.hercules.listacompras.api.model.Status;

import java.util.List;

public interface ItemService {

    public Item adicionarItem(Item novoItem);
    public List<Item> buscarTodosItens();
    public Item buscarItemPorId(Long id);
    public void deletarItem(Long id);
    public List<Item> buscarPorCategoria(Categoria categoria);
    public List<Item> buscarPorStatus(Status status);
}
