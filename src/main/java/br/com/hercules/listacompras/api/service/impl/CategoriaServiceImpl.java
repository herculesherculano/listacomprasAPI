package br.com.hercules.listacompras.api.service.impl;
import br.com.hercules.listacompras.api.dto.CategoriaRequestDTO;
import br.com.hercules.listacompras.api.dto.CategoriaResponseDTO;
import br.com.hercules.listacompras.api.dto.ItemCategoriaRequestDTO;
import br.com.hercules.listacompras.api.dto.ItemCategoriaResponseDTO;
import br.com.hercules.listacompras.api.exception.ResourceAlreadyExistsException;
import br.com.hercules.listacompras.api.exception.ResourceNotFoundException;
import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.repository.CategoriaRepository;
import br.com.hercules.listacompras.api.service.CategoriaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoriaServiceImpl  implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final RestTemplate restTemplate;
    private final String url;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, RestTemplate restTemplate, @Value("${categoria.api.url}") String url ){
        this.categoriaRepository=categoriaRepository;
        this.restTemplate=restTemplate;
        this.url = url;
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

    @Override
    public List<CategoriaResponseDTO> buscarTodasCategorias() {

        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                .map(categoria -> new CategoriaResponseDTO(
                        categoria.getId(),
                        categoria.getNome()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Categoria buscarCategoriaPorNome(String nomeCategoria){
        return categoriaRepository.findByNomeIgnoreCase(nomeCategoria).orElseThrow(()-> new ResourceNotFoundException("Categoria não encontrada"));

    }

    @Override
    public Categoria buscarCategoriaPorId(Long id){
        return categoriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    public String preverCategoria(String nomeItem) {

        ItemCategoriaRequestDTO requestDTO = new ItemCategoriaRequestDTO(nomeItem);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ItemCategoriaRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

        try{
            ResponseEntity<ItemCategoriaResponseDTO> response = restTemplate.postForEntity(url, entity, ItemCategoriaResponseDTO.class);

            if (response == null || response.getBody() == null) {
                throw new IllegalStateException("A previsão de categoria falhou: corpo da resposta da API está nulo.");
            }

            return response.getBody().getCategoria();

        }catch (ResourceAccessException e){
            log.warn("API Python de Previsão de Categoria Indisponível. Usando categoria padrão");
            return "NÃO CATEGORIZADO";
        }
    }
}
