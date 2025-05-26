package br.com.hercules.listacompras.api.repository;

import br.com.hercules.listacompras.api.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
}
