package eventos.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import eventos.modelo.dao.EventoDao;
import eventos.modelo.dao.ReservaDao;
import eventos.modelo.dao.UsuarioDao;
import eventos.modelo.entitis.Evento;
import eventos.modelo.entitis.Reserva;

@Controller
public class EventoController {

	@Autowired
	private EventoDao eDao;

	@Autowired
	private UsuarioDao uDao;

	@Autowired
	private ReservaDao rDao;

	// GETMAPPING Y POSTMAPPING DETALLES

	@GetMapping("/detalles/{id}")
	public String detallesEventos(@PathVariable("id") int idEvento, Model model, Authentication auth) {
		Evento evento = eDao.verUnEvento(idEvento);
		auth = SecurityContextHolder.getContext().getAuthentication();
		String usu = auth.getName();

		if(eDao.verUnEvento(idEvento).getEstado() == "Activo") {
		int cantidadDisponible = evento.getAforoMaximo() - rDao.cantReservas(idEvento);
		int limiteMaximo = 10 - rDao.rvasPorUsuarioYEvento(idEvento, usu);
		model.addAttribute("cantidad", cantidadDisponible);
		model.addAttribute("limiteMaximo", limiteMaximo);
		}
		else {
		
		model.addAttribute("evento", evento);
		model.addAttribute("usuario", usu);
		model.addAttribute("cantidad", 0);
		model.addAttribute("limiteMaximo", 0);
		}
		return "detalles";
	}

	@PostMapping("/detalles/{id}")
	public String realizarReserva(@PathVariable("id") int idEvento, @RequestParam int cantidad,
			@RequestParam String observaciones, Authentication auth) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Evento evento = eDao.verUnEvento(idEvento);

		BigDecimal precioVenta = evento.getPrecio().multiply(BigDecimal.valueOf(cantidad));
		Reserva reserva = new Reserva(cantidad, observaciones, precioVenta, evento, uDao.verUsuario(username));

		rDao.realizarReserva(reserva);
		return "redirect:/misReservas";
	}

	// GETMAPPING Y POSTMAPPING DETALLES EVENTOS DESTACADOS

	@GetMapping("/eventosDestacados/verDetalles/{id}")
	public String detallesEventosDestacados(@PathVariable("id") int idEvento, Model model, Authentication auth) {
		Evento evento = eDao.verUnEvento(idEvento);
		auth = SecurityContextHolder.getContext().getAuthentication();
		String usu = auth.getName();

		if(eDao.verUnEvento(idEvento).getEstado() == "Activo") {
			int cantidadDisponible = evento.getAforoMaximo() - rDao.cantReservas(idEvento);
			int limiteMaximo = 10 - rDao.rvasPorUsuarioYEvento(idEvento, usu);
			model.addAttribute("cantidad", cantidadDisponible);
			model.addAttribute("limiteMaximo", limiteMaximo);
			}
			else {
			
			model.addAttribute("evento", evento);
			model.addAttribute("usuario", usu);
			model.addAttribute("cantidad", 0);
			model.addAttribute("limiteMaximo", 0);
			}
		return "detallesDestacado";
	}

	
	@PostMapping("/eventosDestacados/verDetalles/{id}")
	public String realizarReservaDestacado(@PathVariable("id") int idEvento, @RequestParam int cantidad,
			@RequestParam String observaciones, Authentication auth) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Evento evento = eDao.verUnEvento(idEvento);

		BigDecimal precioVenta = evento.getPrecio().multiply(BigDecimal.valueOf(cantidad));
		Reserva reserva = new Reserva(cantidad, observaciones, precioVenta, evento, uDao.verUsuario(username));

		rDao.realizarReserva(reserva);
		return "redirect:/misReservas";
	}

	// GETMAPPING Y POSTMAPPING DETALLES EVENTOS ACTIVOS

	@GetMapping("/eventosActivos/verDetalles/{id}")
	public String detallesEventosActivos(@PathVariable("id") int idEvento, Model model, Authentication auth) {
		Evento evento = eDao.verUnEvento(idEvento);
		auth = SecurityContextHolder.getContext().getAuthentication();
		String usu = auth.getName();

		if(eDao.verUnEvento(idEvento).getEstado() == "Activo") {
			int cantidadDisponible = evento.getAforoMaximo() - rDao.cantReservas(idEvento);
			int limiteMaximo = 10 - rDao.rvasPorUsuarioYEvento(idEvento, usu);
			model.addAttribute("cantidad", cantidadDisponible);
			model.addAttribute("limiteMaximo", limiteMaximo);
			}
			else {
			
			model.addAttribute("evento", evento);
			model.addAttribute("usuario", usu);
			model.addAttribute("cantidad", 0);
			model.addAttribute("limiteMaximo", 0);
			}
		return "detallesActivos";
	}

	@PostMapping("/eventosActivos/verDetalles/{id}")
	public String realizarReservaActivos(@PathVariable("id") int idEvento, @RequestParam int cantidad,
			@RequestParam String observaciones, Authentication auth) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Evento evento = eDao.verUnEvento(idEvento);

		BigDecimal precioVenta = evento.getPrecio().multiply(BigDecimal.valueOf(cantidad));
		Reserva reserva = new Reserva(cantidad, observaciones, precioVenta, evento, uDao.verUsuario(username));

		rDao.realizarReserva(reserva);
		return "redirect:/misReservas";
	}
}
