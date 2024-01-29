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
	 * Este método ver detalles, muestra una vista con el evento específico y el
	 * usuario.
	 * 
	 * @param idEvento Contiene el valor númerico de la variable asignada en la url
	 * @param model    Es un objeto que se utiliza para pasar información entre el
	 *                 controlador y la vista.
	 * @param usuario  Objeto, el cual esta realizando la solicitud
	 * @return Devolverá la vista "detalles" utilizando la información proporcionada
	 *         en el modelo
	 */

	@GetMapping("/detalles/{id}")
	public String detallesEventos(@PathVariable("id") int idEvento, Model model, Authentication auth, Reserva reserva) {
		Evento evento = eDao.verUnEvento(idEvento);
		auth = SecurityContextHolder.getContext().getAuthentication();
		String usu = auth.getName();

		int cantidadDisponible = evento.getAforoMaximo() - rDao.cantReservas(idEvento);
		int limiteMaximo = 10 - rDao.rvasPorUsuarioYEvento(idEvento, usu);

		model.addAttribute("evento", evento);
		model.addAttribute("usuario", usu);
		model.addAttribute("cantidad", cantidadDisponible);
		model.addAttribute("limiteMaximo", limiteMaximo);

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

	/**
	 * Este método se encarga de mostrar los detalles de un evento destacado,
	 * obteniendo la información necesaria del evento y del usuario actual y luego
	 * pasándola a una vista llamada "detallesDestacado".
	 * 
	 * @param idEvento Es el indentificador propio del evento
	 * @param model    Añade el objeto evento al modelo con el nombre "evento".
	 *                 Permitiendo que la información del evento esté disponible en
	 *                 la vista.
	 * @param usuario  Agrega el objeto usu (información del usuario) al modelo con
	 *                 el nombre "usuario". permitiendo que la información del
	 *                 usuario esté disponible en la vista.
	 * @return Devuelve a la vista que se mostrará después de ejecutar el método. En
	 *         este caso, vista se llama "detallesDestacado".
	 */
	@GetMapping("/eventosDestacados/verDetalles/{id}")
	public String detallesEventosDestacados(@PathVariable("id") int idEvento, Model model, Authentication auth) {
		Evento evento = eDao.verUnEvento(idEvento);
		auth = SecurityContextHolder.getContext().getAuthentication();
		String usu = auth.getName();

		int cantidadDisponible = evento.getAforoMaximo() - rDao.cantReservas(idEvento);
		int limiteMaximo = 10 - rDao.rvasPorUsuarioYEvento(idEvento, usu);

		model.addAttribute("evento", evento);
		model.addAttribute("usuario", usu);
		model.addAttribute("cantidad", cantidadDisponible);
		model.addAttribute("limiteMaximo", limiteMaximo);
		return "detallesDestacado";
	}

	/**
	 * Este método se utiliza para realizar una reserva después de visualizar los
	 * detalles de un evento destacado. Agrega un mensaje de éxito como atributo
	 * flash y redirige al usuario a la página de "misReservas".
	 * 
	 * @param ratt     Agrega un mensaje de éxito como atributo flash ( Reserva
	 *                 realizada con éxito )
	 * @param idEvento Identificador del evento
	 * @param usuario  Objeto con la información del usuario
	 * @return Después de realizar la reserva satisfactoriamente, redirige al
	 *         usuario a la página "/misReservas".
	 */
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

	/**
	 * Este método se encarga de mostrar los detalles de un evento activo,
	 * obteniendo la información necesaria del evento y del usuario actual y luego
	 * pasándola a una vista llamada "detallesActivos".
	 * 
	 * @param idEvento Identificador del evento
	 * @param model    Agrega el objeto evento al modelo con el nombre "evento".
	 *                 Permitiendo que la información del evento esté disponible en
	 *                 la vista.
	 * @param usuario  Objeto con la información del usuario
	 * @return Devuelve el nombre de la vista que se mostrará después de ejecutar
	 *         este método en este caso "detallesActivos".
	 */
	@GetMapping("/eventosActivos/verDetalles/{id}")
	public String detallesEventosActivos(@PathVariable("id") int idEvento, Model model, Authentication auth) {
		Evento evento = eDao.verUnEvento(idEvento);
		auth = SecurityContextHolder.getContext().getAuthentication();
		String usu = auth.getName();

		int cantidadDisponible = evento.getAforoMaximo() - rDao.cantReservas(idEvento);
		int limiteMaximo = 10 - rDao.rvasPorUsuarioYEvento(idEvento, usu);

		model.addAttribute("evento", evento);
		model.addAttribute("usuario", usu);
		model.addAttribute("cantidad", cantidadDisponible);
		model.addAttribute("limiteMaximo", limiteMaximo);
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
