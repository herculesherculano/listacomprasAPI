package br.com.hercules.listacompras.api.service;

import br.com.hercules.listacompras.api.dto.CategoriaResponseDTO;
import br.com.hercules.listacompras.api.dto.ItemRequestDTO;
import br.com.hercules.listacompras.api.dto.ItemResponseDTO;
import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.model.Item;
import br.com.hercules.listacompras.api.model.Status;

import java.util.List;

public interface ItemService {

    public ItemResponseDTO adicionarItem(ItemRequestDTO novoItem);
    public List<ItemResponseDTO> buscarTodosItens();
    public ItemResponseDTO buscarItemPorId(Long id);
    public void deletarItem(Long id);
    public List<ItemResponseDTO> buscarPorCategoria(String nomeCategoria);
    public List<ItemResponseDTO> buscarPorStatus(Status status);
    public ItemResponseDTO alterarItem(Long id, ItemRequestDTO itemAlterado);
    public ItemResponseDTO alterarStatusItem(Long id, Status status);
    public List<ItemResponseDTO> buscarPorCategoriaStatus (String nomeCategoria, Status status);
}
