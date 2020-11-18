package com.bolsadeideas.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeideas.springboot.app.models.dao.IClientDao;
import com.bolsadeideas.springboot.app.models.entity.Client;

@Controller
@SessionAttributes("client")
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
	 * aqui se elimina el atributo de session
	 */
	@PostMapping("/form")
	public String save(@Valid Client client, BindingResult result, Model model, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("title", "Client Form");
			/*
			 * cuando falla pasa la validación, Spring añade el objeto como atributo de
			 * forma automática, siempre y cuando el T tipo de la clase, se llame igual que
			 * el nombre con el cual se está pasando a la vista, en este caso client
			 */
			return "form";
		}
		clientDao.save(client);
		// va a eliminar el objeto cliente de la session
		status.setComplete();
		return "redirect:list";

	}

	/**
	 * Para editar lo hacemos con el parametro id y en la vista list creamos el link
	 * para editar requestMapping sin el metodo por defeto es un GET
	 */
	@RequestMapping(value = "/form/{id}")
	public String edit(@PathVariable(value = "id") Long id, Map<String, Object> model) {
		Client client = null;
		if (id > 0) {
			client = clientDao.findOne(id);
		} else {
			return "redirect:/list";
		}
		model.put("client", client);
		model.put("title", "Edit CLient");
		return "form";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable(value = "id") Long id) {
		if (id > 0) {
			clientDao.delete(id);
		}
		return "redirect:/list";
	}
}
