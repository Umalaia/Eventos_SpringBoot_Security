package eventos.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/signup")
	public String registrar(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "registro";
	}

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
			model.addAttribute("mensaje", "Ya existe como usuario");
			return "redirect:/signup";
		}
	}
	
	//CERRAR SESIÓN
	
	@GetMapping("/signout")
	public String cerrarSesion(Model model) {
		return "redirect:/";
	}
	

	// LOGIN

	@GetMapping("/login")
	public String procesarLogin(Authentication aut, Model model, HttpSession misesion,Usuario usuario) {
		misesion.setAttribute("usuario", usuario);
		return "login";
	}
	
	
	@PostMapping("/login")
	public String procLogin(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpSession misesion, RedirectAttributes ratt) {
		Usuario usuario = uDao.UsuarioYpass(username, password);
		if(usuario != null) {
		    usuario.setPassword(null);
		    usuario.setNombre(username); // Establecer el nombre utilizando el username
		    misesion.setAttribute("usuario", usuario);
		    return "redirect:/home";
		
		}
		ratt.addFlashAttribute("mensaje", "Usuario o Password incorrecto");
		return "redirect:/login";
	}


	// HOME

	@GetMapping({"/", "/home"})
	public String verIndex(Model model, Authentication aut, HttpSession misesion, Usuario usuario) {
		System.out.println(aut.getName() + " - " + aut.getAuthorities());
		
		// Obtener eventos destacados
		List<Evento> destacados = eDao.verEventosDestacados();
		model.addAttribute("destacados", destacados);

		// Obtener todos los tipos de eventos
		List<Tipo> tiposEvento = tDao.todosLosTiposEventos();
		model.addAttribute("TiposEvento", tiposEvento);

		model.addAttribute("usuario", usuario.getNombre());


		return "home";
	}

	
	
	@GetMapping("/eventosActivos")
	public String verActivos(Model model, Authentication aut, HttpSession misesion) {

		// Obtener eventos activos
		List<Evento> activos = eDao.verEventosActivos();
		model.addAttribute("activos", activos);

		return "eventosActivos";
	}

	@GetMapping("/eventosDestacados")
	public String verDestacados(Model model, Authentication aut, HttpSession misesion) {

		List<Evento> destacados = eDao.verEventosDestacados();
		model.addAttribute("destacados", destacados);

		return "eventosDestacados";
	}
	
	
	@GetMapping("/detalles")
	public String verDetalles(Model model, Authentication aut, HttpSession misesion) {

		List<Evento> destacados = eDao.verEventosDestacados();
		model.addAttribute("destacados", destacados);

		return "detalles";
	}

}
