package eventos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import eventos.modelo.dao.ReservaDao;
import eventos.modelo.entitis.Reserva;
import jakarta.servlet.http.HttpSession;

@Controller
public class ReservaController {
	
	@Autowired
	private ReservaDao rDao;
	
	@GetMapping("/misReservas")
	public String verMisReservas(Model model,  Authentication aut, HttpSession misesion) {
		List<Reserva> reservas = rDao.verReservas();
		model.addAttribute("reservas", reservas);
		return "/misReservas";
	}

}
