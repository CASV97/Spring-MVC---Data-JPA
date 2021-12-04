
package com.bolsadeideas.springboot.app;

import java.util.Locale;

//import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

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
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		// asignamos el idioma por defecto de la session, por defecto lo tenemos en
		// ingles
		log.trace("Devolviendo el localeResolver");
		sessionLocaleResolver.setDefaultLocale(new Locale("en", "US"));
		return sessionLocaleResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		log.trace("Cambiando el lenguaje cada vez que se pasa el parametro 'lang' por url");
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.trace("Registrando interceptores");
		registry.addInterceptor(localeChangeInterceptor());

	}

}
