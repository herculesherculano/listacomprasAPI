package br.com.hercules.listacompras.api.service.impl;

import br.com.hercules.listacompras.api.dto.CategoriaRequestDTO;
import br.com.hercules.listacompras.api.dto.CategoriaResponseDTO;
import br.com.hercules.listacompras.api.exception.ResourceAlreadyExistsException;
import br.com.hercules.listacompras.api.exception.ResourceNotFoundException;
import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.repository.CategoriaRepository;
import br.com.hercules.listacompras.api.service.CategoriaService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaServiceImpl  implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository){
        this.categoriaRepository=categoriaRepository;
    }

    @Override
    public CategoriaResponseDTO cadastrarCategoria(CategoriaRequestDTO novaCategoria) {
        Optional<Categoria> existente = categoriaRepository.findByNomeIgnoreCase(novaCategoria.getNome());
        if(existente.isPresent()){
            throw new ResourceAlreadyExistsException("Categoria " + novaCategoria.getNome() + " já existe");
        }
        Categoria categoria = new Categoria();
        categoria.setNome(novaCategoria.getNome());
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return new CategoriaResponseDTO(categoriaSalva.getId(), categoriaSalva.getNome());
    }
}
