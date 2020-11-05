package com.bolsadeideas.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bolsadeideas.springboot.app.models.dao.IClientDao;

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
}
