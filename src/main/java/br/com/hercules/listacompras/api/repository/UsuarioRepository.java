package br.com.hercules.listacompras.api.repository;

import br.com.hercules.listacompras.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public UserDetails findByNomeUsuario(String nomeUsuario);
}
