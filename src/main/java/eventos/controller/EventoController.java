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
import eventos.modelo.dao.UsuarioDao;
import eventos.modelo.entitis.Evento;
import eventos.modelo.entitis.Reserva;
import eventos.modelo.entitis.Usuario;
import jakarta.servlet.http.HttpSession;

@Controller
public class EventoController {

	@Autowired
	private EventoDao eDao;
	
	@Autowired
	private UsuarioDao uDao;
	
	@Autowired
	private ReservaDao rDao;
	
	/**
	 * Este método VER DETALLES, muestra una vista con el evento específico y el usuario
	 * @param idEvento		Contiene el valor númerico de la variable asignada en la url
	 * @param model			Es un objeto que se utiliza para pasar información entre el controlador y la vista.
	 * @param usuario		Objeto el cual esta realizando la solicitud
	 * @return				Devolverá la vista "detalles" utilizando la información proporcionada en el modelo
	 */
	
	@GetMapping("/detalles/{id}")
	public String detallesEventos (@PathVariable ("id") int idEvento, Model model, Usuario usuario, Reserva reserva) {
		Evento evento = eDao.verUnEvento(idEvento);
		Usuario usu = uDao.verUsuario(usuario.getUsername());
		model.addAttribute("evento", evento);
		model.addAttribute("usuario", usu);
		return "detalles";
	}
	
	
	@PostMapping("/detalles/{id}")
	public String postAltaReserva(RedirectAttributes ratt, HttpSession misesion, @PathVariable ("id") int idEvento,
			Evento evento, Usuario usuario, Reserva reserva) {
		Usuario usu = (Usuario)misesion.getAttribute(usuario.getUsername());
		Reserva rva = new Reserva();
		rva.setEvento(evento);
		
		ratt.addFlashAttribute("mensaje", "Reserva realizada con éxito");
		return "redirect:/misReservas";
	}
	
	
	@GetMapping("/eventosDestacados/verDetalles/{id}")
	public String detallesEventosDestacados (@PathVariable ("id") int idEvento, Model model, Usuario usuario) {
		Evento evento = eDao.verUnEvento(idEvento);
		Usuario usu = uDao.verUsuario(usuario.getUsername());
		model.addAttribute("evento", evento);
		model.addAttribute("usuario", usu);
		return "detallesDestacado";
	}
	
	
	@PostMapping("/eventosDestacados/verDetalles/{id}")
	public String postAltaReservaDes(RedirectAttributes ratt, @PathVariable ("id") int idEvento, Usuario usuario) {
		
		ratt.addFlashAttribute("mensaje", "Reserva realizada con éxito");
		return "redirect:/misReservas";
	}
	
	
	
	@GetMapping("/eventosActivos/verDetalles/{id}")
	public String detallesEventosActivos (@PathVariable ("id") int idEvento, Model model, Usuario usuario) {
		Evento evento = eDao.verUnEvento(idEvento);
		Usuario usu = uDao.verUsuario(usuario.getUsername());
		model.addAttribute("evento", evento);
		model.addAttribute("usuario", usu);
		return "detallesActivos";
	}
	
	
	@PostMapping("/eventosActivos/verDetalles/{id}")
	public String postAltaReservaAct(RedirectAttributes ratt, @PathVariable ("id") int idEvento, Usuario usuario) {
		
		ratt.addFlashAttribute("mensaje", "Reserva realizada con éxito");
		return "redirect:/misReservas";
	}
	
	
	
	
	
	
}
