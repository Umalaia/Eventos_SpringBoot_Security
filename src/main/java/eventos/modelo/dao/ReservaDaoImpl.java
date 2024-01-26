package eventos.modelo.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import eventos.modelo.entitis.Evento;
import eventos.modelo.entitis.Reserva;
import eventos.modelo.entitis.Usuario;
import eventos.modelo.repository.ReservaRepository;
@Repository
public class ReservaDaoImpl implements ReservaDao{
	
	@Autowired
	private ReservaRepository rRepo;


	@Override
	public int eliminarReserva(int idReserva) {
		try {
			rRepo.deleteById(idReserva);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public Reserva verUnaReserva(int idReserva) {
		return rRepo.findById(idReserva).orElse(null);
	}


	@Override
	public List<Reserva> verReservas() {
		return rRepo.findAll();
	}

	@Override
	public List<Reserva> verReservasPorUsuario(Usuario usuario) {
		return rRepo.verReservasPorUsuario(usuario);
	}

	
	@Override
	public void realizarReserva(Evento evento, Reserva reserva, Usuario usuario) {
		BigDecimal precio = evento.getPrecio();
		int cantidad = reserva.getCantidad();
		BigDecimal precioTotal = evento.calcularPrecioTotal(precio,cantidad);

        // Crea una nueva reserva
        Reserva rva = new Reserva();
        reserva.setEvento(evento);
        reserva.setCantidad(cantidad);
        reserva.setUsuario(usuario);
        reserva.setPrecioVenta(precioTotal);

        rRepo.save(rva);
    }
	

}
