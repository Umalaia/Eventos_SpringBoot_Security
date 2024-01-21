package eventos.modelo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eventos.modelo.entitis.Evento;

public interface EventoRepository extends JpaRepository<Evento, Integer>{
	
	@Query
	("Select e from Evento e where e.destacado = S")
	public List<Evento> verEventosDestacados(int idEvento);
	
	@Query
	("Select e from Evento e where e.estado = Activo")
	public List<Evento> verEventosActivos(int idEvento);

}
