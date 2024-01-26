package eventos.modelo.dao;

import java.util.List;

import eventos.modelo.entitis.Evento;
import eventos.modelo.entitis.Reserva;
import eventos.modelo.entitis.Usuario;

public interface ReservaDao {
	int eliminarReserva(int idReserva);
	Reserva verUnaReserva(int idReserva);
	List<Reserva> verReservas();
	List<Reserva> verReservasPorUsuario( Usuario usuario);
	void realizarReserva(Evento evento, Reserva reserva, Usuario usuario);

}
