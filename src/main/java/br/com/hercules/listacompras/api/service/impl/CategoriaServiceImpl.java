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
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
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
    @Override
    public Categoria buscarCategoriaPorNome(String nomeCategoria){
        return categoriaRepository.findByNomeIgnoreCase(nomeCategoria).orElseThrow(()-> new ResourceNotFoundException("Categoria não encontrada"));

    }

    @Override
    public Categoria buscarCategoriaPorId(Long id){
        return categoriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    public String preverCategoria(String nomeItem) {
        String url=  "http://127.0.0.1:8000/prever/";

        RestTemplate restTemplate = new RestTemplate();
        ItemCategoriaRequestDTO requestDTO = new ItemCategoriaRequestDTO(nomeItem);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ItemCategoriaRequestDTO> entity = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<ItemCategoriaResponseDTO> response = restTemplate.postForEntity(url, entity, ItemCategoriaResponseDTO.class);

        System.out.println("🔍 Status: " + response.getStatusCode());
        System.out.println("🔍 Body: " + response.getBody());
        System.out.println(response.getBody().getCategoria());

        if (response.getBody() == null) {
            throw new IllegalStateException("A resposta veio nula da API Python.");
        }
        return response.getBody().getCategoria();
    }
}
