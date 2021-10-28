/**
 * 
 */
package com.bolsadeideas.springboot.app.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

/**
 * @author CASV97
 *
 */
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	/**/
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// Administrador de Map para los mensajes Flash
		SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
		// Crear un flash con los mensajes del tipo FlashMap
		FlashMap flashMap = new FlashMap();
		flashMap.put("success", "Hi!!.. " + authentication.getName() + " Session initialized successfully");
		// registramos el flashMap en el Mannager
		flashMapManager.saveOutputFlashMap(flashMap, request, response);
		// utilizamos objeto logger de la clase padre
		// SimpleUrlAuthenticationSuccessHandler
		if (authentication != null) {
			logger.info("the user '" + authentication.getName() + "' has logged successfully!!");

		}

		super.onAuthenticationSuccess(request, response, authentication);
	}

}
