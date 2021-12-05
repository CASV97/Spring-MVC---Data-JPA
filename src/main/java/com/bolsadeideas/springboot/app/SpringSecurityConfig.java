/**
 * 
 */
package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccessHandler;
import com.bolsadeideas.springboot.app.models.service.JpaUserDetailService;

/**
 * Esta clase de congfiguracion de Spring es para Spring security, de debe
 * extender de {@code WebSecurityConfigurerAdapter},
 * 
 * @author CASV97
 */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private LoginSuccessHandler successHandler;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JpaUserDetailService userDetailsService;

	/**
	 * Añadiendo método configure(HttpSecurity http) para las reglas ACL en las
	 * rutas invocando el objeto http vamos a invocar el metodo authorizeRequest() y
	 * para comenzar con el método antMatchers, para asignar nuestras rutas, todo lo
	 * que sea de acceso publico, con permitAll(), para las rutas privadas o que
	 * queremos proteger
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/img/**","/locale").permitAll()

				.anyRequest().authenticated().and()
				// Con el nombre de la ruta o el getmapping de la ruta
				.formLogin().successHandler(successHandler).loginPage("/login").permitAll().and().logout().permitAll()
				.and().exceptionHandling().accessDeniedPage("/error_403");
	}

	/**
	 * para autenticar mediante JDBC se realiza consulta
	 * 
	 * @param {@code {@link AuthenticationManagerBuilder}} Para registrar nuestros
	 *               usuarios
	 */
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
}
