package eventos.modelo.dao;

import java.util.List;

import eventos.modelo.entitis.Evento;

public interface EventoDao {
	List<Evento> todosLosEventos();
	Evento verUnEvento(int idEvento);
	List<Evento> verEventosDestacados(int idEvento);
	List<Evento> verEventosActivos(int idEvento);	

}
