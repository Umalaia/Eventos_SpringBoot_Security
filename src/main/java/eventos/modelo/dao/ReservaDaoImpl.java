package eventos.modelo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import eventos.modelo.entitis.Reserva;
import eventos.modelo.entitis.Usuario;
import eventos.modelo.repository.ReservaRepository;
@Repository
public class ReservaDaoImpl implements ReservaDao{
	
	@Autowired
	private ReservaRepository rRepo;

	@Override
	public Reserva altaReserva(Reserva reserva) {
		try {
			return rRepo.save(reserva);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int eliminarReserva(int idReserva) {
		try {
			rRepo.deleteById(idReserva);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<Reserva> verReservas() {
		return rRepo.findAll();
	}

	@Override
	public List<Reserva> verReservasPorUsuario(Usuario usuario) {
		return rRepo.verReservasPorUsuario(usuario);
	}

	@Override
	public Reserva verUnaReserva(int idReserva) {
		return rRepo.findById(idReserva).orElse(null);
	}

}
