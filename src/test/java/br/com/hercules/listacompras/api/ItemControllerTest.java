package br.com.hercules.listacompras.api;

import br.com.hercules.listacompras.api.controller.ItemController;
import br.com.hercules.listacompras.api.dto.CategoriaResponseDTO;
import br.com.hercules.listacompras.api.dto.ItemRequestDTO;
import br.com.hercules.listacompras.api.dto.ItemResponseDTO;
import br.com.hercules.listacompras.api.exception.ResourceNotFoundException;
import br.com.hercules.listacompras.api.model.Status;
import br.com.hercules.listacompras.api.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static br.com.hercules.listacompras.api.model.Status.COMPRADO;
import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static br.com.hercules.listacompras.api.model.Status.PENDENTE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    void setUp(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void deveAdicionarItemComSucesso(){
        //Given
        ItemResponseDTO responseDTO = new ItemResponseDTO(1L, "Arroz", 1.0, new CategoriaResponseDTO(1L, "MERCEARIA"), PENDENTE);
        ItemRequestDTO  requestDTO = new ItemRequestDTO("Arroz", 1.0, PENDENTE);
        when(itemService.adicionarItem(any(ItemRequestDTO.class))).thenReturn(responseDTO);

        //When
        ResponseEntity<ItemResponseDTO> resultado = itemController.adicionarItem(requestDTO);

        //Then
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertThat(resultado.getBody()).usingRecursiveComparison().isEqualTo(responseDTO);
        assertNotNull(resultado.getHeaders().getLocation());
        assertTrue(resultado.getHeaders().getLocation().toString().contains("/1"));
    }

    @Test
    void deveBuscarTodosItens(){
        //Given
        CategoriaResponseDTO categoria = new CategoriaResponseDTO(1L, "MERCEARIA");
        List<ItemResponseDTO> responseDTO = List.of(
                new ItemResponseDTO(1L,"Arroz",2.0,categoria, PENDENTE),
                new ItemResponseDTO(2L,"Feijão",1.0,categoria, PENDENTE),
                new ItemResponseDTO(3L,"Sal",1.0,categoria, PENDENTE),
                new ItemResponseDTO(4L,"Açucar",3.0,categoria, PENDENTE)
        );
        when(itemService.buscarTodosItens()).thenReturn(responseDTO);

        //When
        ResponseEntity<List<ItemResponseDTO>> resultado = itemController.buscarTodosItens();

        //Then
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertThat(resultado.getBody()).usingRecursiveComparison().isEqualTo(responseDTO);
    }

    @Test
    void deveBuscarItemPorID(){
        //Given
        CategoriaResponseDTO categoria = new CategoriaResponseDTO(1L, "MERCEARIA");
        ItemResponseDTO responseDTO = new ItemResponseDTO(1L,"Arroz",2.0,categoria, PENDENTE);
        when(itemService.buscarItemPorId(1L)).thenReturn(responseDTO);

        //When
        ResponseEntity<ItemResponseDTO> resultado = itemController.buscarItemPorId(1L);

        //Then
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertThat(resultado.getBody()).usingRecursiveComparison().isEqualTo(responseDTO);
    }

    @Test
    void deveLancarExececaoBuscarItemInexistente(){
        //Given
        when(itemService.buscarItemPorId(anyLong())).thenThrow(new ResourceNotFoundException("item não encontrado"));

        //When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> itemController.buscarItemPorId(1L));

        //Then
        assertThat(exception.getMessage()).isEqualTo("item não encontrado");
    }

    @Test
    void deveBuscarItemPorCategoria(){
        //Given
        CategoriaResponseDTO categoria = new CategoriaResponseDTO(1L, "MERCEARIA");
        List<ItemResponseDTO> responseDTO = List.of(
                new ItemResponseDTO(1L,"Arroz",2.0,categoria, PENDENTE),
                new ItemResponseDTO(2L,"Feijão",1.0,categoria, PENDENTE),
                new ItemResponseDTO(3L,"Sal",1.0,categoria, PENDENTE),
                new ItemResponseDTO(4L,"Açucar",3.0,categoria, PENDENTE)
        );
        when(itemService.buscarPorCategoria("MERCEARIA")).thenReturn(responseDTO);

        //When
        ResponseEntity<List<ItemResponseDTO>> resultado = itemController.buscarPorCategoria("MERCEARIA");

        //Then
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertThat(resultado.getBody()).usingRecursiveComparison().isEqualTo(responseDTO);
    }

    @Test
    void deveLancarExececaoBuscarItemCategoriaInexistente(){
        //Given
        when(itemService.buscarPorCategoria(anyString())).thenThrow(new ResourceNotFoundException("Categoria não encontrada"));

        //When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> itemController.buscarPorCategoria("MERCEARIA"));

        //Then
        assertThat(exception.getMessage()).isEqualTo("Categoria não encontrada");
    }

    @Test
    void deveBuscarItemPorStatus() {
        //Given
        CategoriaResponseDTO categoria = new CategoriaResponseDTO(1L, "MERCEARIA");
        List<ItemResponseDTO> responseDTO = List.of(
                new ItemResponseDTO(1L, "Arroz", 2.0, categoria, PENDENTE),
                new ItemResponseDTO(2L, "Feijão", 1.0, categoria, PENDENTE),
                new ItemResponseDTO(3L, "Sal", 1.0, categoria, PENDENTE),
                new ItemResponseDTO(4L, "Açucar", 3.0, categoria, PENDENTE)
        );
        when(itemService.buscarPorStatus(PENDENTE)).thenReturn(responseDTO);

        //When
        ResponseEntity<List<ItemResponseDTO>> resultado = itemController.buscarPorStatus(PENDENTE);

        //Then
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertThat(resultado.getBody()).usingRecursiveComparison().isEqualTo(responseDTO);
    }

    @Test
    void deveBuscarPorCategoriaStatus(){
        //Given
        CategoriaResponseDTO categoria = new CategoriaResponseDTO(1L, "MERCEARIA");
        List<ItemResponseDTO> responseDTO = List.of(
                new ItemResponseDTO(1L, "Arroz", 2.0, categoria, PENDENTE),
                new ItemResponseDTO(2L, "Feijão", 1.0, categoria, PENDENTE),
                new ItemResponseDTO(3L, "Sal", 1.0, categoria, PENDENTE),
                new ItemResponseDTO(4L, "Açucar", 3.0, categoria, PENDENTE)
        );
        when(itemService.buscarPorCategoriaStatus("MERCEARIA",PENDENTE)).thenReturn(responseDTO);

        //When
        ResponseEntity<List<ItemResponseDTO>> resultado = itemController.buscarPorCategoriaStatus("MERCEARIA",PENDENTE);

        //Then
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertThat(resultado.getBody()).usingRecursiveComparison().isEqualTo(responseDTO);
    }

    @Test
    void deveAlterarItemPorId(){
        //Given
        ItemResponseDTO responseDTO = new ItemResponseDTO(1L, "Arroz", 1.0, new CategoriaResponseDTO(1L, "MERCEARIA"), PENDENTE);
        ItemRequestDTO  requestDTO = new ItemRequestDTO("Arroz", 1.0, PENDENTE);
        when(itemService.alterarItem(1L, requestDTO)).thenReturn(responseDTO);

        //When
        ResponseEntity<ItemResponseDTO> resultado = itemController.alterarItem(1L, requestDTO);
        //Then
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertThat(resultado.getBody()).usingRecursiveComparison().isEqualTo(responseDTO);
    }

    @Test
    void deveLancarExececaoAlterarItemInexistente(){
        //Given
        ItemRequestDTO  requestDTO = new ItemRequestDTO("Arroz", 1.0, PENDENTE);
        when(itemService.alterarItem(anyLong(), any(ItemRequestDTO.class))).thenThrow(new ResourceNotFoundException("item não encontrado"));

        //When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> itemController.alterarItem(1L, requestDTO));

        //Then
        assertThat(exception.getMessage()).isEqualTo("item não encontrado");
    }

    @Test
    void deveAlterarStatusItem(){
        //Given
        ItemResponseDTO responseDTO = new ItemResponseDTO(1L, "Arroz", 1.0, new CategoriaResponseDTO(1L, "MERCEARIA"), COMPRADO);
        when(itemService.alterarStatusItem(1L, COMPRADO)).thenReturn(responseDTO);

        //When
        ResponseEntity<ItemResponseDTO> resultado = itemController.alterarStatusItem(1L, COMPRADO);

        //Then
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertThat(resultado.getBody()).usingRecursiveComparison().isEqualTo(responseDTO);
    }

    @Test
    void deveLancarExcecaoAlterarStatusItemInexistente(){
        //Given
        when(itemService.alterarStatusItem(anyLong(), any(Status.class))).thenThrow(new ResourceNotFoundException("Item não encontrado"));

        //When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> itemController.alterarStatusItem(1L, COMPRADO));

        //Then
        assertThat(exception.getMessage()).isEqualTo("Item não encontrado");
    }

    @Test
    void deveDeletarItemPorId(){
        //Given
        doNothing().when(itemService).deletarItem(1L);

        //When
        ResponseEntity<Void> resultado = itemController.deletarItem(1L);

        //Then
        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        assertNull(resultado.getBody());

    }

    @Test
    void deveLancarExcecaoDeletarItemInexistente(){
        //Given
        doThrow( new ResourceNotFoundException("Item não encontrado")).when(itemService).deletarItem(999L);

        //When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> itemController.deletarItem(999L));

        //Then
        assertThat(exception.getMessage()).isEqualTo("Item não encontrado");


    }

}
