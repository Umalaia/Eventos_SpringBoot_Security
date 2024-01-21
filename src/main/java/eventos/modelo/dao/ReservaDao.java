package eventos.modelo.dao;

import java.util.List;

import eventos.modelo.entitis.Reserva;
import eventos.modelo.entitis.Usuario;

public interface ReservaDao {
	Reserva altaReserva(Reserva reserva);
	int eliminarReserva(int idReserva);
	Reserva verUnaReserva(int idReserva);
	List<Reserva> verReservas();
	List<Reserva> verReservasPorUsuario( Usuario usuario);

}
