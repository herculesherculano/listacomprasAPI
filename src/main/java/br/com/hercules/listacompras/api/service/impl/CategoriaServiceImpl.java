package br.com.hercules.listacompras.api.service.impl;

import br.com.hercules.listacompras.api.dto.CategoriaRequestDTO;
import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.repository.CategoriaRepository;
import br.com.hercules.listacompras.api.service.CategoriaService;
import org.springframework.stereotype.Service;

@Service
public class CategoriaServiceImpl  implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository){
        this.categoriaRepository=categoriaRepository;
    }

    @Override
    public Categoria cadastrarCategoria(CategoriaRequestDTO novaCategoria) {
        Categoria categoria = new Categoria();
        categoria.setNome(novaCategoria.getNome());
        return categoriaRepository.save(categoria);
    }
}
