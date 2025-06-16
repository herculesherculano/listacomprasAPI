package br.com.hercules.listacompras.api;


import br.com.hercules.listacompras.api.dto.CategoriaRequestDTO;
import br.com.hercules.listacompras.api.dto.CategoriaResponseDTO;
import br.com.hercules.listacompras.api.dto.ItemCategoriaRequestDTO;
import br.com.hercules.listacompras.api.dto.ItemCategoriaResponseDTO;
import br.com.hercules.listacompras.api.exception.ResourceAlreadyExistsException;
import br.com.hercules.listacompras.api.exception.ResourceNotFoundException;
import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.repository.CategoriaRepository;
import br.com.hercules.listacompras.api.service.impl.CategoriaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;


public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        String fakeUrl="http://127.0.0.1:8000/prever/";
        categoriaService = new CategoriaServiceImpl(
                categoriaRepository,
                restTemplate,
                fakeUrl
        );
    }

    @Test
    void deveCadastrarCategoria(){
        //Dado(Given)
        CategoriaRequestDTO categoriaRequestDTO = new CategoriaRequestDTO("MERCEARIA");
        when(categoriaRepository.findByNomeIgnoreCase("MERCEARIA")).thenReturn(Optional.empty());

        Categoria categoria = new Categoria(1L, "MERCEARIA");
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);
        //Quando(When)
        CategoriaResponseDTO resultado = categoriaService.cadastrarCategoria(categoriaRequestDTO);

        //Então(Then)
        assertThat(resultado.getNome()).isEqualTo("MERCEARIA");
        verify(categoriaRepository, times(1)).save(any(Categoria.class));

    }

    @Test
    void deveRetornarExcecaoCategoriaJaExistente(){
        //Dado(Given)
        Categoria categoria = new Categoria(1L, "MERCEARIA");
        CategoriaRequestDTO categoriaRequestDTO = new CategoriaRequestDTO("MERCEARIA");
        when(categoriaRepository.findByNomeIgnoreCase("MERCEARIA")).thenReturn(Optional.of(categoria));

        //Quando(When)
        RuntimeException exception = assertThrows(ResourceAlreadyExistsException.class, () ->categoriaService.cadastrarCategoria(categoriaRequestDTO));

        //Então(Then)
        assertThat(exception.getMessage()).isEqualTo("Categoria " + categoria.getNome() + " já existe");
        verify(categoriaRepository, never()).save(any());

    }

    @Test
    void deveBuscarCategoriaPorNome(){
        //Dado(Given)
        Categoria categoria = new Categoria(1L,"MERCEARIA");
        when(categoriaRepository.findByNomeIgnoreCase("MERCEARIA")).thenReturn(Optional.of(categoria));

        //Quando(When)
        Categoria resultado = categoriaService.buscarCategoriaPorNome("MERCEARIA");

        //Então(Then)
        assertThat(resultado.getNome()).isEqualTo("MERCEARIA");
        verify(categoriaRepository, times(1)).findByNomeIgnoreCase("MERCEARIA");
    }

    @Test
    void deveRetornarExcecaoNomeCategoriaInexistente(){
        //Dado(Given)
        when(categoriaRepository.findByNomeIgnoreCase("MERCEARIA")).thenReturn(Optional.empty());

        //Quando(When)
        RuntimeException exception = assertThrows(ResourceNotFoundException.class, () -> categoriaService.buscarCategoriaPorNome("MERCEARIA"));

        //Então(Then)
        assertThat(exception.getMessage()).isEqualTo("Categoria não encontrada");
    }

    @Test
    void deveBuscarCategoriaPorId(){
        //Dado(Given)
        Categoria categoria = new Categoria(1L, "MERCEARIA");
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        //Quando(When)
        Categoria resultado = categoriaService.buscarCategoriaPorId(1L);

        //Então(Then)
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("MERCEARIA");
    }

    @Test
    void deveRetornarExecaoAoBuscarIdInexistente(){
        //Dado(Given)
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        //Quando(When)

        RuntimeException exception = assertThrows(ResourceNotFoundException.class, () -> categoriaService.buscarCategoriaPorId(1L));

        //Então(Then)

        assertThat(exception.getMessage()).isEqualTo("Categoria não encontrada");

    }

    @Test
    void devePreverCategoriaComSucesso(){
        //Given
        ItemCategoriaResponseDTO responseBody = new ItemCategoriaResponseDTO("MERCEARIA");
        ResponseEntity<ItemCategoriaResponseDTO> fakeResponse = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(ItemCategoriaResponseDTO.class)
        )).thenReturn(fakeResponse);

        //Quando(When)
        String categoria = categoriaService.preverCategoria("Arroz");

        //Então(Then)
        assertThat(categoria).isEqualTo("MERCEARIA");
    }

    @Test
    void deveRetornarExcecaoRespostaNula(){
        //Given
        ResponseEntity<ItemCategoriaResponseDTO> fakeResponse = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(ItemCategoriaResponseDTO.class)
        )).thenReturn(fakeResponse);

        RuntimeException exception = assertThrows(IllegalStateException.class, () -> categoriaService.preverCategoria("Arroz"));

        assertThat(exception.getMessage()).isEqualTo("A previsão de categoria falhou: corpo da resposta da API está nulo.");
    }
}


