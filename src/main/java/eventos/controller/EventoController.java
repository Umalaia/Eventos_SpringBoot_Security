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
	/**
	 * Este código maneja solicitudes GET a la ruta "/detalles/{id}", recupera
	 * detalles de un evento, información de autenticación del usuario, realiza
	 * cálculos relacionados con reservas y luego pasa esos datos a la vista
	 * "detalles".
	 * 
	 * @param idEvento El valor de la variable propio
	 * @param model    Para almacenar datos y pasarlos a la vista.
	 * @param auth     Para obtener información sobre la autenticación del usuario.
	 * @return El método devuelve el nombre de la vista que se mostrará al usuario.
	 *         En este caso, vista "detalles".
	 */
	@GetMapping("/detalles/{id}")
	public String detallesEventos(@PathVariable("id") int idEvento, Model model, Authentication auth) {
		Evento evento = eDao.verUnEvento(idEvento);
		auth = SecurityContextHolder.getContext().getAuthentication();
		String usu = auth.getName();

		int cantidadDisponible = evento.getAforoMaximo() - rDao.cantReservas(idEvento);
		int limiteMaximo = 10 - rDao.rvasPorUsuarioYEvento(idEvento, usu);
		model.addAttribute("cantidad", cantidadDisponible);
		model.addAttribute("limiteMaximo", limiteMaximo);
		model.addAttribute("evento", evento);
		model.addAttribute("usuario", usu);

		return "detalles";
	}

	/**
	 * Este código maneja solicitudes POST a la ruta "/detalles/{id}", realiza una
	 * reserva para un evento específico con la cantidad y observaciones
	 * proporcionadas, y luego redirige al usuario a la página de sus reservas.
	 * 
	 * @param idEvento      Indica el valor de la variable
	 * @param cantidad      Indica el valor de la variable cantidad solicitado
	 * @param observaciones Indica el valor de la variable observaciones solicitado
	 * @param auth          Se utiliza para obtener información sobre la
	 *                      autenticación del usuario.
	 * @return Después de realizar la reserva, se le redirige al usuario a la página
	 *         "/misReservas".
	 */

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
	/**
	 * Este código maneja solicitudes GET a la ruta
	 * "/eventosDestacados/verDetalles/{id}", recupera detalles de un evento,
	 * información de autenticación del usuario, realiza cálculos relacionados con
	 * reservas y luego pasa esos datos a la vista "detallesDestacado".
	 * 
	 * @param idEvento Indica el valor de la variable
	 * @param model    Se utiliza para almacenar datos y pasarlos a la vista.
	 * @param auth     Se utiliza para obtener información sobre la autenticación
	 *                 del usuario.
	 * @return El método devuelve el nombre de la vista que se mostrará al usuario.
	 *         En este caso, vista "detallesDestacado".
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
	 * Este método maneja solicitudes POST a la ruta
	 * "/eventosDestacados/verDetalles/{id}", realiza una reserva para un evento
	 * destacado específico con la cantidad y observaciones proporcionadas, y luego
	 * redirige al usuario a la página de sus reservas.
	 * 
	 * @param idEvento      Indica el valor de la variable
	 * @param cantidad      Indica el valor de la variable cantidad solicitado
	 * @param observaciones Indica el valor de la observaciones también solicitado
	 * @param auth          Se utiliza para obtener información sobre la
	 *                      autenticación del usuario.
	 * @return Después de realizar la reserva, la aplicación redirige al usuario a
	 *         la página "/misReservas".
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

	// GETMAPPING Y POSTMAPPING DETALLES EVENTOS ACTIVOS
	/**
	 * Este método maneja solicitudes GET a la ruta
	 * "/eventosActivos/verDetalles/{id}", recupera detalles de un evento activo,
	 * información de autenticación del usuario, realiza cálculos relacionados con
	 * reservas y luego pasa esos datos a la vista "detallesActivos".
	 * 
	 * @param idEvento Indica el valor de la variable
	 * @param model    Se utiliza para almacenar datos y pasarlos a la vista.
	 * @param auth     Se utiliza para obtener información sobre la autenticación
	 *                 del usuario.
	 * @return El método devuelve el nombre de la vista que se mostrará al usuario.
	 *         En este caso, vista "detallesActivos".
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

	/**
	 * Este método maneja solicitudes POST a la ruta
	 * "/eventosActivos/verDetalles/{id}", realiza una reserva para un evento activo
	 * específico con la cantidad y observaciones proporcionadas, y luego redirige
	 * al usuario a la página de sus reservas.
	 * 
	 * @param idEvento      Indica el valor de la variable
	 * @param cantidad      Indica el valor de la variable cantidad solicitado
	 * @param observaciones Indica el valor de la observaciones también solicitado
	 * @param auth          Se utiliza para obtener información sobre la
	 *                      autenticación del usuario.
	 * @return Este método maneja solicitudes POST a la ruta
	 *         "/eventosActivos/verDetalles/{id}", realiza una reserva para un
	 *         evento activo específico con la cantidad y observaciones
	 *         proporcionadas, y luego redirige al usuario a la página de sus
	 *         reservas.
	 */
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
