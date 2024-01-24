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
import eventos.modelo.dao.ReservaDao;
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
	ReservaDao rDao;
	@Autowired
	PerfilDao pDao;

	// REGISTRO

	@GetMapping("/signup")
	public String registrar(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "Registro";
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
			return "/Registro";
		}
	}

	// LOGIN

/*
	@GetMapping("/login")
	public String procesarLogin(Authentication aut, Model model, HttpSession misesion) {
		return "login";
	}
	
	
	@PostMapping("/login")
	public String procLogin(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpSession misesion, RedirectAttributes ratt) {
		Usuario usuario = uDao.UsuarioYpass(username, password);
		if(usuario != null) {
			usuario.setPassword(null);
			misesion.setAttribute("usuario", usuario);
			return "redirect:/Index";
		}
		ratt.addFlashAttribute("mensaje", "Usuario o Password incorrecto");
		return "redirect:/Index";
	}
*/

	// HOME

	@GetMapping("/Index")
	public String verIndex(Model model, Authentication aut, HttpSession misesion, @PathVariable("id") String username) {
		System.out.println(aut.getName() + " - " + aut.getAuthorities());
		
		// Obtener eventos destacados
		List<Evento> destacados = eDao.verEventosDestacados();
		model.addAttribute("destacados", destacados);

		// Obtener todos los tipos de eventos
		List<Tipo> tiposEvento = tDao.todosLosTiposEventos();
		model.addAttribute("TiposEvento", tiposEvento);

		model.addAttribute("usuario", misesion.getAttribute(username));

		return "/";
	}

	
	@GetMapping("/EventosActivos")
	public String verActivos(Model model, Authentication aut, HttpSession misesion) {

		// Obtener eventos activos
		List<Evento> activos = eDao.verEventosActivos();
		model.addAttribute("activos", activos);

		return "EventosActivos";
	}

	@GetMapping("/EventosDestacados")
	public String verDestacados(Model model, Authentication aut, HttpSession misesion) {

		List<Evento> destacados = eDao.verEventosDestacados();
		model.addAttribute("destacados", destacados);

		return "EventosDestacados";
	}

}
