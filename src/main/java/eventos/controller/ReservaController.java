package eventos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import eventos.modelo.dao.EventoDao;
import eventos.modelo.dao.ReservaDao;
import eventos.modelo.dao.UsuarioDao;
import eventos.modelo.entitis.Evento;
import eventos.modelo.entitis.Reserva;
import eventos.modelo.entitis.Usuario;
import jakarta.servlet.http.HttpSession;

@Controller
public class ReservaController {
	
	@Autowired
	private ReservaDao rDao;
	
	@Autowired
	private UsuarioDao uDao;
	
	@Autowired
	private EventoDao eDao;
	
	
	@GetMapping("/misReservas")
	public String verMisReservas(Model model, Authentication aut, HttpSession misesion, Usuario usuario) {
	    if (aut != null && aut.isAuthenticated()) {
	        String nombreUsuario = aut.getName();

	        Usuario user = uDao.verUsuario(nombreUsuario);
	        List<Reserva> reservas = rDao.verReservasPorUsuario(user);
	        model.addAttribute("reservas", reservas);
	        return "misReservas";
	    } else {
	        return "redirect:/login";
	    }
	}
	
	@GetMapping("/misReservas/eliminar/{id}")
	public String eliminarReserva(@PathVariable ("id") int idReserva, Model model) {
		if(rDao.eliminarReserva(idReserva) == 1) {
		model.addAttribute("mensaje", "Se ha eliminado la reserva");
		return "redirect:/misReservas";
		}else
			model.addAttribute("mensaje", "No ha podido eliminar la reserva");
		return "redirect:/misReservas";
	}
	
    
}
