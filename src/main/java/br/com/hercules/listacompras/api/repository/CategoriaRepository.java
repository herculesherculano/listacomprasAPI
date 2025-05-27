package br.com.hercules.listacompras.api.repository;

import br.com.hercules.listacompras.api.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
