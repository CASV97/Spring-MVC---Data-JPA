
package com.bolsadeideas.springboot.app;

//import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
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
	 * <h3>addViewControllers(registry)</h3>
	 * 
	 * Registrar Controladores parametrizableso estáticos que simplemente cargan la
	 * vista y no tienen lógica del controlador ({@code ViewControllerRegistry} )
	 * 
	 * @param {@code {@link ViewControllerRegistry}}
	 */
	public void addViewControllers(ViewControllerRegistry registry) {
		// Con esto ya tenemos registrado el controlador parametrizable
		log.trace("\n Registrando Controladores parametrizables estáticos sin logica");
		registry.addViewController("error_403").setViewName("requestErrors/error_403");
	}

	/* Ejemplo para subir imagenes a un directorio estático */
	/*
	 * @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
	 * WebMvcConfigurer.super.addResourceHandlers(registry);
	 * 
	 * String resourcePath =
	 * Paths.get("uploads").toAbsolutePath().toUri().toString();
	 * log.info(resourcePath); // 1 Usamos la misma ruta a la que accede la vista
	 * registry.addResourceHandler("/uploads/**") // 2 la ubicación, como es una
	 * ruta un directorio físico usamos file // que lo agrega automaticamente
	 * .addResourceLocations(resourcePath); }
	 */

}
