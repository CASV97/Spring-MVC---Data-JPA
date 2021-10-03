/**
 * 
 */
package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Esta clase de congfiguracion de Spring es para Spring security, de debe
 * extender de {@code WebSecurityConfigurerAdapter},
 * 
 * @author ariel
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	/**
	 * Registramos el password encoder como por defecto en nuestra configuracion de
	 * spring security, con este password encoder podemos crear los usuarios y
	 * encriptar su contraseña, este metodo quedará disponible dentro del contexto
	 * de spring security
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	/**
	 * Tenemos que registrar un método para poder registrar y configurar los
	 * usuarios de nuestro sistema de seguridad
	 * 
	 * @param {@code {@link AuthenticationManagerBuilder}} Para registrar nuestros
	 *               usuarios
	 */
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
		// para versiones anteriores de Spring boot
		// UserBuilder users=User.withDefaultPasswordEncoder();
		PasswordEncoder encoder = passwordEncoder();
		// para las nuevas versiones y lleva una funcion lambda por que por cada usuario
		// que registremos se genera un evento que va a encriptar la contraseña
		// (parametro -> return)
		UserBuilder users = User.builder().passwordEncoder(password -> encoder.encode(password));
		// Con withUser declaramo los nuevos usuarios y el método password se encarga de
		// encriptar el pass
		builder.inMemoryAuthentication().withUser(users.username("admin").password("admin").roles("ADMIN", "USER"))
				.withUser(users.username("ariel").password("ariel").roles("USER"));
	}

}
