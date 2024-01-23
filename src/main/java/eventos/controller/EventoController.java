package eventos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import eventos.modelo.dao.EventoDao;
import eventos.modelo.entitis.Evento;

@Controller
public class EventoController {

	@Autowired
	private EventoDao eDao;
	
	@GetMapping("/EventosDestacados/verDetalles/{id}")
	public String detallesEventosDestacados (@PathVariable ("id") int idEvento, Model model) {
		Evento evento = eDao.verUnEvento(idEvento);
		model.addAttribute("evento", evento);
		return "/detallesDestacado";
	}
	
	@GetMapping("/EventosActivos/verDetalles/{id}")
	public String detallesEventosActivos (@PathVariable ("id") int idEvento, Model model) {
		Evento evento = eDao.verUnEvento(idEvento);
		model.addAttribute("evento", evento);
		return "/detallesActivos";
	}
	
	
	
}
