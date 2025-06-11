package br.com.hercules.listacompras.api;

import br.com.hercules.listacompras.api.dto.ItemRequestDTO;
import br.com.hercules.listacompras.api.dto.ItemResponseDTO;
import br.com.hercules.listacompras.api.exception.ResourceNotFoundException;
import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.model.Item;
import br.com.hercules.listacompras.api.repository.ItemRepository;
import br.com.hercules.listacompras.api.service.CategoriaService;
import br.com.hercules.listacompras.api.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.List;
import java.util.Optional;

import static br.com.hercules.listacompras.api.model.Status.COMPRADO;
import static br.com.hercules.listacompras.api.model.Status.PENDENTE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveAdicionarItem(){
        //Dado (Given)
        String nomeItem= "Feijão";
        Categoria categoria = new Categoria(1L,"MERCEARIA");

        when(categoriaService.preverCategoria(nomeItem)).thenReturn("MERCEARIA");
        when(categoriaService.buscarCategoriaPorNome("MERCEARIA")).thenReturn(categoria);

        Item itemSalvo= new Item();
        itemSalvo.setNome("Feijão");
        itemSalvo.setQuantidade(10.0);
        itemSalvo.setCategoria(categoria);
        itemSalvo.setStatus(PENDENTE);
        when(itemRepository.save(any(Item.class))).thenReturn(itemSalvo);

        ItemRequestDTO itemRequest = new ItemRequestDTO();
        itemRequest.setNome("Feijão");
        itemRequest.setQuantidade(10.0);
        itemRequest.setStatus(PENDENTE);

        //Quando(When)

        ItemResponseDTO resultado = itemService.adicionarItem(itemRequest);

        //Então(Then)
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Feijão");
        assertThat(resultado.getQuantidade()).isEqualTo(10.0);
        assertThat(resultado.getStatus()).isEqualTo(PENDENTE);
        assertThat(resultado.getCategoria().getNome()).isEqualTo("MERCEARIA");
        verify(itemRepository, times(1)).save(any(Item.class));

    }

    @Test
    void deveBuscarTodosItens(){
        //Dado(Given)
        Categoria categoria = new Categoria(1L, "MERCEARIA");
        List<Item> itensList = List.of(
                new Item(1L,"Arroz",2.0,categoria, PENDENTE),
                new Item(2L,"Feijão",1.0,categoria, PENDENTE),
                new Item(3L,"Sal",1.0,categoria, PENDENTE),
                new Item(4L,"Açucar",3.0,categoria, PENDENTE)
        );
        when(itemRepository.findAll()).thenReturn(itensList);

        //Quando(When)
        List<ItemResponseDTO> resultado = itemService.buscarTodosItens();

        //Então(Then)
        assertThat(resultado).hasSize(itensList.size());
        assertThat(resultado.getFirst().getNome()).isEqualTo("Arroz");
        assertThat(resultado.getFirst().getQuantidade()).isEqualTo(2.0);
        assertThat(resultado.get(1).getNome()).isEqualTo("Feijão");
        assertThat(resultado.get(1).getQuantidade()).isEqualTo(1.0);
        assertThat(resultado.get(2).getNome()).isEqualTo("Sal");
        assertThat(resultado.get(2).getQuantidade()).isEqualTo(1.0);
        assertThat(resultado.get(3).getNome()).isEqualTo("Açucar");
        assertThat(resultado.get(3).getQuantidade()).isEqualTo(3.0);
        for (ItemResponseDTO dto: resultado){
            assertThat(dto.getCategoria().getNome()).isEqualTo("MERCEARIA");
            assertThat(dto.getStatus()).isEqualTo(PENDENTE);
        }
        verify(itemRepository, times(1)).findAll();

    }

    @Test
    void deveBuscarItemPorId(){
        //Dado(Given)
        Categoria categoria = new Categoria(1L,"MERCEARIA");
        Item item = new Item(1L, "Feijão", 10.0, categoria, PENDENTE);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        //When
         ItemResponseDTO resultado = itemService.buscarItemPorId(1L);

         //Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getNome()).isEqualTo("Feijão");
        assertThat(resultado.getQuantidade()).isEqualTo(10.0);
        assertThat(resultado.getStatus()).isEqualTo(PENDENTE);
        assertThat(resultado.getCategoria().getNome()).isEqualTo("MERCEARIA");

    }

    @Test
    void deveLancarExcecaoQuandoItemNaoExistir(){
            when(itemRepository.findById(999L)).thenReturn(Optional.empty());
            RuntimeException exception = assertThrows(ResourceNotFoundException.class, () -> itemService.buscarItemPorId(999L));

            assertThat(exception.getMessage()).isEqualTo("Item não encontrado");
    }

    @Test
    void deveExcluirItemPorId(){
        //Dado(Given)
        when(itemRepository.existsById(1L)).thenReturn(true);
        doNothing().when(itemRepository).deleteById(1L);

        //Quando(When)
        itemService.deletarItem(1L);

        //Então(Then)
        verify(itemRepository, times(1)).deleteById(1L);

    }

    @Test
    void deveLancarExcecaoAoDeletarItemInexistente(){
        //Dado(Given)
        when(itemRepository.existsById(999L)).thenReturn(false);

        //Quando(When)
        RuntimeException exception = assertThrows(ResourceNotFoundException.class, () -> itemService.deletarItem(999L));

        //Então(Then)
        assertThat(exception.getMessage()).isEqualTo("Item não encontrado");
    }

    @Test
    void deveBuscarItensPorCategoria(){
        //Dado(Given)
        Categoria categoria = new Categoria(1L, "MERCEARIA");
        List<Item> itensList = List.of(
                new Item(1L,"Arroz",2.0,categoria, PENDENTE),
                new Item(2L,"Feijão",1.0,categoria, PENDENTE),
                new Item(3L,"Sal",1.0,categoria, PENDENTE),
                new Item(4L,"Açucar",3.0,categoria, PENDENTE)
        );
        when(categoriaService.buscarCategoriaPorNome("MERCEARIA")).thenReturn(categoria);
        when(itemRepository.findByCategoria(categoria)).thenReturn(itensList);

        //Quando(When)
        List<ItemResponseDTO> resultado = itemService.buscarPorCategoria("MERCEARIA");

        //Então(Then)
        assertThat(resultado).hasSize(itensList.size());
        for (ItemResponseDTO dto : resultado){
            assertThat(dto.getCategoria().getNome()).isEqualTo("MERCEARIA");
        }
    }

    @Test
    void deveBuscarItensPorStatus(){
        //Dado(Given)
        Categoria categoria = new Categoria(1L, "MERCEARIA");
        List<Item> itensList = List.of(
                new Item(1L,"Arroz",2.0,categoria, PENDENTE),
                new Item(2L,"Feijão",1.0,categoria, PENDENTE),
                new Item(3L,"Sal",1.0,categoria, PENDENTE),
                new Item(4L,"Açucar",3.0,categoria, PENDENTE)
        );
        when(itemRepository.findByStatus(PENDENTE)).thenReturn(itensList);

        //Quando(When)
        List<ItemResponseDTO> resultado = itemService.buscarPorStatus(PENDENTE);

        //Então(Then)
        assertThat(resultado).hasSize(itensList.size());
        for(ItemResponseDTO dto : resultado){
            assertThat(dto.getStatus()).isEqualTo(PENDENTE);
        }
    }

    @Test
    void deveBuscarItensPorCategoriaEStatus(){
        //Dado(Given)
        Categoria categoria = new Categoria(1L, "MERCEARIA");
        List<Item> itensList = List.of(
                new Item(1L,"Arroz",2.0,categoria, PENDENTE),
                new Item(2L,"Feijão",1.0,categoria, PENDENTE),
                new Item(3L,"Sal",1.0,categoria, PENDENTE),
                new Item(4L,"Açucar",3.0,categoria, PENDENTE)
        );
        when(categoriaService.buscarCategoriaPorNome("MERCEARIA")).thenReturn(categoria);
        when(itemRepository.findByCategoriaAndStatus(categoria, PENDENTE)).thenReturn(itensList);

        //Quando(When)
        List<ItemResponseDTO> resultado = itemService.buscarPorCategoriaStatus("MERCEARIA", PENDENTE);

        //Então(Then)
        assertThat(resultado).hasSize(itensList.size());
        for (ItemResponseDTO dto : resultado){
            assertThat(dto.getCategoria().getNome()).isEqualTo("MERCEARIA");
            assertThat(dto.getStatus()).isEqualTo(PENDENTE);
        }

    }

    @Test
    void deveAlterarItem(){

        //Dado(Given)
        Categoria categoria = new Categoria(1L,"MERCEARIA");
        Categoria categoria2 = new Categoria(2L, "FRIOS E LATICÍNIOS");
        Item item = new Item(1L, "Feijão", 10.0, categoria, PENDENTE);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(categoriaService.buscarCategoriaPorId(2L)).thenReturn(categoria2);
        ItemRequestDTO requestDTO = new ItemRequestDTO("Presunto", 5.0, 2L, COMPRADO);

        //Quando(When)

        ItemResponseDTO resultado = itemService.alterarItem(1L, requestDTO);

        //Então(Then)

        assertThat(resultado.getNome()).isEqualTo("Presunto");
        assertThat(resultado.getQuantidade()).isEqualTo(5.0);
        assertThat(resultado.getCategoria().getNome()).isEqualTo("FRIOS E LATICÍNIOS");
        assertThat(resultado.getStatus()).isEqualTo(COMPRADO);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void deveAlterarStatusDoItem() {
        //Dado(Given)
        Categoria categoria = new Categoria(1L, "MERCEARIA");
        Item item = new Item(1L, "Feijão", 10.0, categoria, PENDENTE);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        //Quando(When)
        ItemResponseDTO resultado = itemService.alterarStatusItem(1L, COMPRADO);

        //Então(Then)
        assertThat(resultado.getStatus()).isEqualTo(COMPRADO);
        verify(itemRepository, times(1)).save(item);
    }

}
