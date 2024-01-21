package eventos.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eventos.modelo.entitis.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	
	@Query
	("select u from usuario u where u.username = ?1")
	Usuario buscarUsuario (String username);

}
