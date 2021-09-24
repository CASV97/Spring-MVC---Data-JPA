package com.bolsadeideas.springboot.app.controllers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.dao.IInvoiceDao;
import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.entity.Invoice;
import com.bolsadeideas.springboot.app.models.entity.InvoiceLineItem;
import com.bolsadeideas.springboot.app.models.entity.Product;
import com.bolsadeideas.springboot.app.models.service.IClientService;

@Controller
@RequestMapping("/invoice")
@SessionAttributes("invoice")
public class InvoinceController {
	@Autowired
	private IClientService clientService;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@GetMapping("/show/{id}")
	public String show(@PathVariable Long id, Model model, RedirectAttributes flash) {
		Invoice invoice = clientService.fecthInvoiceByIdWithClientWithInvoiceLineItemWithProduct(id);
		if (invoice == null) {
			flash.addFlashAttribute("error", "The invoice does not exist!");
			return "redirect:/list";

		}

		model.addAttribute("invoice", invoice);
		model.addAttribute("title", "Invoice: ".concat(invoice.getDescription()));

		return "invoice/show";

	}

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

	/**
	 * Tiene que ser el mismo mapping que la petición Ajax, ademas indicamos que va
	 * a tener una salida del tipo application/json por que produce una resppuesta
	 * del tipo Json, y por último el método @ResponseBody, entonces esta anotación
	 * lo que hace es suprimir el cargar una vista de thymeleaf, en vez de eso va a
	 * tomar el resultado convertido a JSON
	 */
	@GetMapping(value = "/load-products/{term}", produces = { "application/json" })
	public @ResponseBody List<Product> loadProducts(@PathVariable String term) {

		return clientService.findByName(term);
	}

	/**
	 * Recibir parametros del Request de la vista items-templates items_id[]
	 * quantity[]
	 * 
	 * @param Invoice    objeto formulario que o mapeado al formulario, que se
	 *                   inyecta de forma automática al metodo contiene todos los
	 *                   datos del formulario exepto las líneas que se manejan a
	 *                   parte a travez de un imput ya que no estan directamente
	 *                   mapeadas a campos de la clase Invoice
	 * @param item_id[]
	 * @param quantity[]
	 * @return View finalmente redirigimos al detalle del cliente, para mostrar el
	 *         listado de sus facturas y le concatenamos el Id del cliente que lo
	 *         obtenemos a travez de la factura
	 * 
	 */
	@PostMapping("/form")
	public String save(@Valid Invoice invoice, BindingResult result, Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "quantity[]", required = false) Integer[] quantity, RedirectAttributes flash,
			SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("title", "Create Invoice");
			return "invoice/form";

		}
		if (itemId == null || itemId.length == 0) {
			model.addAttribute("title", "Create Invoice");
			model.addAttribute("error", "Error: The invoice cannot be empty, don´t have any lines");
			return "invoice/form";
		}
		for (int i = 0; i < itemId.length; i++) {
			Product product = clientService.findProductById(itemId[i]);
			InvoiceLineItem line = new InvoiceLineItem();
			line.setProduct(product);
			line.setQuantity(quantity[i]);
			invoice.addInvoiceLineItem(line);
			log.info("ID: " + itemId[i].toString() + ", Qantity: " + quantity[i].toString());
		}
		clientService.saveIvoice(invoice);

		status.setComplete();
		flash.addFlashAttribute("success", "Invoice was create successfuly");
		return "redirect:/show/" + invoice.getClient().getId();
	}

	/**
	 * Eliminar la factura, esté método retona un String un 'redirect:' , y para
	 * devolver un mensaje al la vista se añade el parámetro RedirectAttributtes
	 */
	@GetMapping("/delete/{id}")
	public String deleteInvoice(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		// Traemos a la factura simplemente para validad
		Invoice invoice = clientService.findInvoiceById(id);
		if (invoice != null) {
			clientService.deleteInvoice(id);
			flash.addFlashAttribute("success", " Invoice has deleted successufully");
			return "redirect:/show/" + invoice.getClient().getId();
		}
		flash.addFlashAttribute("error", "Invoice Can`t delete");
		return "redirect:/list";
	}

}
