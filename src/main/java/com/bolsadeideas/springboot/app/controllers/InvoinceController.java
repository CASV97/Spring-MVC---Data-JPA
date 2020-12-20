package com.bolsadeideas.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.entity.Invoice;
import com.bolsadeideas.springboot.app.models.service.IClientService;

@Controller
@RequestMapping("/invoice")
@SessionAttributes("invoice")
public class InvoinceController {
	@Autowired
	private IClientService clientService;

	/**
	 * Metodo para crear una factura en la vista, es importante mantener el objeto
	 * factura dentro de una session mientras que se procesa el formulario, por eso
	 * es importante agrefar la anotacion @SeesionAttributes
	 */
	@GetMapping("/form/{clientId}")
	public String Create(@PathVariable(name = "clientId") Long clientId, Model model, RedirectAttributes flash) {
		Client client = clientService.findOne(clientId);
		if (client == null) {
			flash.addFlashAttribute("error", "Client id doesn't exist in database");
			return "redirect:/list";
		}
		// recordando que un cliente puede tener un cliente y un cliente puede tener
		// muchas facturas
		Invoice invoice = new Invoice();
		invoice.setClient(client);
		model.addAttribute("invoice", invoice);
		model.addAttribute("title", "Create Invoice");

		return "invoice/form";
	}

}
