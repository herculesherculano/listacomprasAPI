package br.com.hercules.listacompras.api.service;

import br.com.hercules.listacompras.api.dto.CategoriaRequestDTO;
import br.com.hercules.listacompras.api.model.Categoria;

public interface CategoriaService {

    public Categoria cadastrarCategoria(CategoriaRequestDTO novaCategoria);

}
