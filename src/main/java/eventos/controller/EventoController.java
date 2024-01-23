package eventos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import eventos.modelo.dao.EventoDao;
import eventos.modelo.dao.ReservaDao;
import eventos.modelo.entitis.Evento;

@Controller
public class EventoController {

	@Autowired
	private EventoDao eDao;
	
	@Autowired
	private ReservaDao rDao;
	
	//VER DETALLES
	
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
	
	
	//ALTA 
	
	@GetMapping("/EventosDestacados/verDetalles/{id}/alta")
	public String altaReservaDes(Model model, @PathVariable ("id") int idEvento) {
		return "/detallesDestacado";
	}
	
	//FALTA POSTMAPPING
	
	@PostMapping("/EventosDestacados/verDetalles/{id}/alta")
	public String postAltaReservaDes(RedirectAttributes ratt) {
		
		ratt.addFlashAttribute("mensaje", "Reserva realizada con éxito");
		return "redirect:/misReservas";
	}
	
	
	@GetMapping("/EventosActivos/verDetalles/{id}/alta")
	public String altaReservaAc(Model model, @PathVariable ("id") int idEvento) {
		return "/detallesActivos";
	}
	
	
	//FALTA POSTMAPPING
	
	@PostMapping("/EventosActivos/verDetalles/{id}/alta")
	public String postAltaReservaAct(RedirectAttributes ratt) {
		
		ratt.addFlashAttribute("mensaje", "Reserva realizada con éxito");
		return "redirect:/misReservas";
	}
	
	
	
}
