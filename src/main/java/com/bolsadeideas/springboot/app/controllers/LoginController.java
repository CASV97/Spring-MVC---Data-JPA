package com.bolsadeideas.springboot.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	/**
	 * Recondamos que el filtro o interceptor de Spring cuando iniciamos sesion,
	 * automaticamente captura la URL /login y obtiene el username y el password
	 * para realizar el proceso de autenticacion y si es correcto, accedemos a las
	 * paginas autorizadas y si sale mal podemos personalizar la respuesta en esta
	 * misma opcion,vamos a renderizar la vista login y ademas tenemos que validar
	 * por medio del Objeto {@link Principal} que contiene el usuario logueado
	 */
	@GetMapping("/login")
	public String login(Model model, Principal principal, ModelAndView modelAndView, RedirectAttributes flash,
			@RequestParam(name = "error", required = false) String error,
			@RequestParam(name = "logout", required = false) String logout) {
		/*
		 * si el objeto Principal != null, es por que ya ha iniciado sesion
		 * anteriormente para que no vuelva a iniciar sesion y tampoco vuelva a mostrar
		 * la pagina de login, esto es para evitar que haga doble inicio de sesion
		 */
		if (principal != null) {
			// El usuario ya ha iniciado sesion
			flash.addFlashAttribute("info", "user already logged in!!");
			return "redirect:/";
		}
		if (error != null) {
			model.addAttribute("error", "Login error, Username or password incorrect, please try again.");
		}
		if (logout!=null) {
			model.addAttribute("success", "Has logged out successfully");
		}

		return "login";
	}
}
