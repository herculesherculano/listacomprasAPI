package br.com.hercules.listacompras.api;

import br.com.hercules.listacompras.api.controller.CategoriaController;
import br.com.hercules.listacompras.api.dto.CategoriaRequestDTO;
import br.com.hercules.listacompras.api.dto.CategoriaResponseDTO;
import br.com.hercules.listacompras.api.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    @BeforeEach
    void setUp(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void deveCadastrarCategoria(){
        //Given
        CategoriaRequestDTO requestDTO = new CategoriaRequestDTO("MERCEARIA");
        CategoriaResponseDTO responseDTO = new CategoriaResponseDTO(1L,"MERCEARIA");
        when(categoriaService.cadastrarCategoria(any(CategoriaRequestDTO.class))).thenReturn(responseDTO);

        //When
        ResponseEntity<CategoriaResponseDTO> resultado = categoriaController.cadastrarCategoria(requestDTO);

        //Then
        assertThat(HttpStatus.CREATED).isEqualTo(resultado.getStatusCode());
        assertThat(resultado.getBody()).usingRecursiveComparison().isEqualTo(responseDTO);
        assertNotNull(resultado.getHeaders().getLocation());
        assertTrue(resultado.getHeaders().getLocation().toString().contains("/1"));

    }
}
