package com.bolsadeideas.springboot.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.bolsadeideas.springboot.app.models.dao.IClientDao;
import com.bolsadeideas.springboot.app.models.entity.Client;

@Controller
public class ClientController {
	@Autowired
	@Qualifier("clientDaoJPA")
	private IClientDao clientDao;

	@GetMapping("/list")
	public String showClientsList(Model model) {
		model.addAttribute("title", "Client List");
		model.addAttribute("clients", clientDao.findAll());
		return "clientlist";
	}

	/**
	 * Se encarga de mostrar el formulario al usuario, y va a recibir el objeto
	 * Model, o puede recibir un Map de java.util, en vez de usar addAttribute,
	 * vamos a usar put
	 */
	@GetMapping("/form")
	public String create(Map<String, Object> model) {
		// creamos la instancia del objeto entidad que esta mapeado a la tabla de la
		// base de datos tanto como al formulario
		Client client = new Client();

		model.put("client", client);
		model.put("title", "Client's form");
		return "form";
	}

	/**
	 * Como segunda fase cuando el usuario envía con un SUBMIT los datos del
	 * formulario, vamos a tener un metodo que se encarge de procesar estos datos
	 */
	@PostMapping("/form")
	public String save(Client client) {
		clientDao.save(client);
		return "redirect:list";

	}
}
