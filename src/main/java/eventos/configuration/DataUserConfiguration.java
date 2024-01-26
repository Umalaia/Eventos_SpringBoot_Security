package eventos.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@EnableWebSecurity
@Configuration
public class DataUserConfiguration {

	@Bean
	public UserDetailsManager usersCustom(DataSource dataSource) {

		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
		users.setUsersByUsernameQuery("select username,password,enabled from Usuarios u where username=?");
		users.setAuthoritiesByUsernameQuery("select u.username,p.nombre from Usuario_Perfiles up "
				+ "inner join usuarios u on u.username = up.username "
				+ "inner join perfiles p on p.id_perfil = up.id_perfil " + "where u.username = ?");

		return users;

	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());
		// Los recursos estáticos no requieren autenticación

		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/static/**").permitAll()
				.requestMatchers("/css/Index.css").permitAll()
				.requestMatchers("/img/Logo.png").permitAll()
				.requestMatchers("/home").permitAll()
				// Las vistas públicas no requieren autenticación
				.requestMatchers("/", "/signup", "/login", "/logout").permitAll()
				.requestMatchers("/eventosDestacados", "/eventosActivos", "/detalles/**").permitAll()
				.requestMatchers("/eventosActivos/verDetalles/**", "/eventosDestacados/verDetalles/**").permitAll()
				.requestMatchers("/rest/encriptar/**").permitAll()
				// Todas las demás URLs de la Aplicación requieren autenticación
				// Asignar permisos a URLs por ROLES
				//.requestMatchers("/#").hasAnyAuthority("ROLE_CLIENTE")
				//.requestMatchers("/misReservas").hasAnyAuthority("ROLE_CLIENTE")
				//.requestMatchers("/signout").hasAnyAuthority("ROLE_CLIENTE")

				.anyRequest().authenticated())
				// El formulario de Login no requiere autenticacion
				.formLogin(form -> form.loginPage("/login").permitAll());
		return http.build();
	}

	/*
	 * @Bean public PasswordEncoder passwordEncoder() { return new
	 * BCryptPasswordEncoder(); }
	 */
	

}
