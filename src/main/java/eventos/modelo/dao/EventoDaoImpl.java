package eventos.modelo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import eventos.modelo.entitis.Evento;
import eventos.modelo.repository.EventoRepository;
@Repository
public class EventoDaoImpl implements EventoDao {
	
	@Autowired
	private EventoRepository eRepo;

	@Override
	public List<Evento> todosLosEventos() {
		return eRepo.findAll();
	}

	@Override
	public Evento verUnEvento(int idEvento) {
		return eRepo.findById(idEvento).orElse(null);
	}

	@Override
	public List<Evento> verEventosDestacados(int idEvento) {
		return eRepo.verEventosDestacados(idEvento);
	}

	@Override
	public List<Evento> verEventosActivos(int idEvento) {
		return eRepo.verEventosActivos(idEvento);
	}
	
}
