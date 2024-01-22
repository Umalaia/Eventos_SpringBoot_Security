package eventos.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	//REGISTRO
	
	/*
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
	 	usuario.setPassword("{noop}"+ usuario.getPassword());
	 	if (uDao.registro(usuario)) {
	 		ratt.addFlashAttribute("mensaje", "alta usuario realizada");
	 		return "redirect:/login";
	 	}
	 	else {
	 		model.addAttribute("mensaje", "ya existe como usuario");
	 		return "/registro";
	 	}
	}
	
	
	//LOGIN
	
	@GetMapping("/login")
	public String procesarLogin(Authentication aut, Model model, HttpSession misesion) {
		
		System.out.println("usuario : " + aut.getName());
		Usuario usuario = uDao.verUsuario(aut.getName());
		
		if (misesion.getAttribute("usuario") == null)
			misesion.setAttribute("usuario", usuario);
		System.out.println();
		
		for (GrantedAuthority ele: aut.getAuthorities())
			System.out.println("ROL : " + ele.getAuthority());
		model.addAttribute("mensaje", aut.getAuthorities());
		
		return "redirect:/";
	}
	
	*/
	
	//HOME
	
	@GetMapping("/")
	public String verEventosDestacados(Model model) {
		//System.out.println(aut.getName() + "  -  " + aut.getAuthorities());
	
		List<Evento> eventosDestacados = eDao.verEventosDestacados();
		model.addAttribute("Listado de eventos destacados", eventosDestacados);
	
		List<Evento> eventosActivos = eDao.verEventosDestacados();
		model.addAttribute("Listado de eventos destacados", eventosActivos);
		
		List<Tipo> tiposEvento = tDao.todosLosTiposEventos();
		model.addAttribute("Tipos de eventos", tiposEvento);
	
		return "Index.html";	 
	}
	
	

}
