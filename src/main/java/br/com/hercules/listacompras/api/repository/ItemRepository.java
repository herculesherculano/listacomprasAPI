package br.com.hercules.listacompras.api.repository;

import br.com.hercules.listacompras.api.model.Categoria;
import br.com.hercules.listacompras.api.model.Item;
import br.com.hercules.listacompras.api.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Stack;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {

    public List<Item> findByCategoria(Categoria categoria);
    public List<Item> findByStatus(Status status);

}
