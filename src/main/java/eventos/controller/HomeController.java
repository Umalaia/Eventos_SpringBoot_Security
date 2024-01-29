package eventos.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import eventos.modelo.dao.EventoDao;
import eventos.modelo.dao.PerfilDao;
import eventos.modelo.dao.TipoDao;
import eventos.modelo.dao.UsuarioDao;
import eventos.modelo.entitis.Evento;
import eventos.modelo.entitis.Tipo;
import eventos.modelo.entitis.Usuario;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	UsuarioDao uDao;
	@Autowired
	EventoDao eDao;
	@Autowired
	TipoDao tDao;
	@Autowired
	PerfilDao pDao;

	// REGISTRO
	/**
	 * Este método se utiliza para mostrar un formulario de registro cuando un
	 * usuario visita la URL "/signup". Inicializa un objeto Usuario vacío y lo pasa
	 * al modelo, luego redirige a la vista "registro".
	 * 
	 * @param model Parámetro, que se utiliza para pasar datos a la vista.
	 * @return Devuelve el nombre de la vista que se mostrará después de ejecutar
	 *         este método. "registro"
	 */
	@GetMapping("/signup")
	public String registrar(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "registro";
	}

	/**
	 * Este método se utiliza para procesar solicitudes de registro de nuevos
	 * usuarios. Configura la información del usuario, intenta registrar al usuario
	 * y redirige al usuario a la página de inicio de sesión o de registro
	 * dependiendo del resultado del registro.
	 * 
	 * @param model   Parámetro, que se utiliza para pasar datos a la vista.
	 * @param usuario Objeto con la información de Usuario. usuario.setEnabled(1);:
	 *                Establece el estado del usuario como habilitado. El valor 1
	 *                significa "habilitado". usuario.setFechaRegistro(new Date());
	 *                Establece la fecha de registro del usuario como la fecha
	 *                actual. usuario.addPerfil(pDao.verUnPerfil(3)); Añade un
	 *                perfil(cliente) al usuario. usuario.setPassword("{noop}" +
	 *                usuario.getPassword()); Configura la contraseña del usuario.
	 *                {noop} significa no encifrado o encriptado
	 * @return Redirige al usuario de vuelta a la página de registro ("/signup").
	 */
	@PostMapping("/signup")
	public String registrar(Model model, Usuario usuario, RedirectAttributes ratt) {
		usuario.setEnabled(1);
		usuario.setFechaRegistro(new Date());
		usuario.addPerfil(pDao.verUnPerfil(3));
		usuario.setPassword("{noop}" + usuario.getPassword());
		if (uDao.registro(usuario)) {
			ratt.addFlashAttribute("mensaje", "Alta usuario realizada");
			return "redirect:/login";
		} else {
			ratt.addFlashAttribute("mensaje", "Ya existe como usuario");
			return "redirect:/signup";
		}
	}

	// CERRAR SESIÓN
	/**
	 * Este método se utiliza para cerrar la sesión de un usuario. Elimina la
	 * información del usuario de la sesión y luego invalida la sesión antes de
	 * redirigir al usuario a la página de inicio.
	 * 
	 * @param misesion Utilizado para controlar la sesión habilitada
	 *                 misesion.removeAttribute("usuario");Elimina el atributo
	 *                 llamado "usuario" de la sesión. Esta información del usuario
	 *                 se almacena en la sesión con el nombre de atributo "usuario".
	 * 
	 *                 misesion.invalidate(); Invalida la sesión, y cierra la sesión
	 *                 actual del usuario.
	 * @return Después de cerrar la sesión, redirige al usuario a la página de
	 *         inicio ("/home").
	 */
	@GetMapping("/signout")
	public String cerrarSesion(HttpSession misesion) {
		misesion.removeAttribute("usuario");
		misesion.invalidate();
		return "redirect:/home";
	}

	// LOGIN
	/**
	 * Este método se utiliza para manejar solicitudes GET a la página de login.
	 * Guarda la información del usuario en la sesión y luego redirige a la vista
	 * "login"
	 * 
	 * @param aut      Toma información del objeto Usuario autentificado
	 * @param model    Utilizado pasar datos a la vista.
	 * @param misesion Usado para para manejar la sesión
	 *                 misesion.setAttribute("usuario", usuario); Almacena el objeto
	 *                 usuario en la sesión con el nombre de atributo "usuario". la
	 *                 información del usuario se guarda en la sesión para ser
	 *                 utilizada en sesiones posteriores.
	 * @param usuario  Objeto usuario con la información del mismo.
	 * @return Devuelve el nombre de la vista que se mostrará después de ejecutar
	 *         este método Por lo tanto, cuando se visita la URL "/login", se
	 *         renderizará la vista de login.
	 */
	@GetMapping("/login")
	public String procesarLogin(Authentication aut, Model model, HttpSession misesion, Usuario usuario) {
		misesion.setAttribute("usuario", usuario);
		return "login";
	}

	/**
	 * Este método maneja solicitudes POST para el inicio de sesión. Intenta
	 * autenticar al usuario con el nombre de usuario y la contraseña
	 * proporcionados, Sí la autenticación es exitosa, almacena la información del
	 * usuario en la sesión y redirige al usuario a la página de inicio. Si la
	 * autenticación falla, agrega un mensaje de error y redirige al usuario de
	 * vuelta a la página de login.
	 * 
	 * @param username Parametro donde indica el nombre
	 * @param password Parametro donde indica el password
	 * @param misesion Objeto para controlar la sesión.
	 * @param ratt     Agrega un mensaje de error como atributo flash para mostrarlo
	 *                 después de la redirección.
	 * @return Redirige al usuario a la página de login ("/login").
	 */
	@PostMapping("/login")
	public String procLogin(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpSession misesion, RedirectAttributes ratt) {
		Usuario usuario = uDao.UsuarioYpass(username, password);
		if (usuario != null) {
			usuario.setPassword(null);
			usuario.setNombre(username);
			misesion.setAttribute("usuario", usuario);
			return "redirect:/home";
		} else
			ratt.addFlashAttribute("mensaje", "Usuario o Password incorrecto");
		return "redirect:/login";
	}

	
	// HOME

	@GetMapping({ "/", "/home" })
	public String verIndex(Model model, Authentication aut, HttpSession misesion, Tipo tipo) {
		if (aut != null && aut.isAuthenticated()) {
			System.out.println(aut.getName() + " - " + aut.getAuthorities());
		} else {
			model.addAttribute("usuario", "Invitado");
		}

		if (tipo.getIdTipo() == 0) {
			List<Evento> destacados = eDao.verEventosDestacados();
			model.addAttribute("destacados", destacados);

			List<Evento> activos = eDao.verEventosActivos();
			model.addAttribute("activos", activos);
		} else {

			List<Evento> destacados = eDao.verDestacadosPorTipo(tipo.getIdTipo());
			model.addAttribute("destacados", destacados);

			List<Evento> activos = eDao.verActivosPorTipo(tipo.getIdTipo());
			model.addAttribute("activos", activos);
		}

		List<Tipo> tiposEvento = tDao.todosLosTiposEventos();
		model.addAttribute("TiposEvento", tiposEvento);

		return "home";
	}

	/**
	 * Este método maneja solicitudes GET para mostrar una lista de eventos activos.
	 * Obtiene la lista de eventos activos a través de un objeto eDao y la agrega al
	 * modelo antes de redirigir al usuario a la vista "eventosActivos".
	 * 
	 * @param model    Para pasar datos a la vista
	 * @return Devuelve el nombre de la vista que se mostrará después de ejecutar
	 *         este método. cuando se visita la URL "/eventosActivos", se
	 *         renderizará la vista "eventosActivos".
	 */
	@GetMapping("/eventosActivos")
	public String verActivos(Model model) {

		List<Evento> activos = eDao.verEventosActivos();
		model.addAttribute("activos", activos);

		return "eventosActivos";
	}

	/**
	 * Este método maneja solicitudes GET para mostrar una lista de eventos
	 * destacados. Obtiene la lista de eventos destacados a través de un objeto eDao
	 * y la agrega al modelo antes de redirigir al usuario a la vista
	 * "eventosDestacados".
	 * 
	 * @param model    Para pasar datos a la vista
	 * @return Devuelve el nombre de la vista que se mostrará después de ejecutar
	 *         este método. Por lo tanto, cuando se visita la URL
	 *         "/eventosDestacados", se renderizará la vista "eventosDestacados".
	 */
	@GetMapping("/eventosDestacados")
	public String verDestacados(Model model) {

		List<Evento> destacados = eDao.verEventosDestacados();
		model.addAttribute("destacados", destacados);

		return "eventosDestacados";
	}

	/**
	 * Este método maneja solicitudes GET para mostrar detalles de eventos. Obtiene
	 * la lista de eventos destacados a través de un objeto eDao y la agrega al
	 * modelo antes de redirigir al usuario a la vista "detalles".
	 * 
	 * @param model    Para pasar datos a la vista
	 * @param aut      Objeto para manejar la información de autenticación del
	 *                 usuario
	 * @param misesion Objeto para controlar la sesión.
	 * @return Devuelve el nombre de la vista que se mostrará después de ejecutar
	 *         este método. cuando se visita la URL "/detalles", se renderizará la
	 *         vista "detalles".
	 */
	@GetMapping("/detalles")
	public String verDetalles(Model model) {

		List<Evento> destacados = eDao.verEventosDestacados();
		model.addAttribute("destacados", destacados);

		return "detalles";
	}

}
