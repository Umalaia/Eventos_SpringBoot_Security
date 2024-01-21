package eventos.modelo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eventos.modelo.entitis.Reserva;
import eventos.modelo.entitis.Usuario;

public interface ReservaRepository extends JpaRepository<Reserva, Integer>{
	
	@Query
	("select r from Reserva r where r.username = ?1")
	public List<Reserva> verReservasPorUsuario(Usuario usuario);

}
