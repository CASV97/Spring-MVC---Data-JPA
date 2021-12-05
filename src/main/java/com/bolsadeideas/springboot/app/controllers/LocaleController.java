/**
 * 
 */
package com.bolsadeideas.springboot.app.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @author CASV97
 *
 */
@Controller
public class LocaleController {
	/** Encargado de manejar la peticion y va a retornar una ruta para redirigir */
	@GetMapping("/locale")
	public String locale(HttpServletRequest request) {
		// obtenermos la referencia de la ultima url
		String lastUrl = request.getHeader("referer");

		return "redirect:".concat(lastUrl);
	}
}
