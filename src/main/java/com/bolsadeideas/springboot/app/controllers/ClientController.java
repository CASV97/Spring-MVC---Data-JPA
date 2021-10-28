package com.bolsadeideas.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.service.IClientService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("client")
public class ClientController {
	@Autowired
	private IClientService clientService;

	@Autowired
	private IUploadFileService uploadFileService;
	// logger copiado de AbstractAuthenticationTargetUrlRequestHandler
	protected final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * Esta Url es la misma que teníamos antes solo que agrega un parámetro que va a
	 * tener una extension ':.+', Esta exprecion regular permite que Spring no borre
	 * o no trunque la extensión del archivo, ya que cuando detecta que la url
	 * termina en '.ext' la va a truncar cuando se envíe un parámetro para poder
	 * pasar solamente el valor del el nombre del parámetro pero sin la extension y
	 * es importante la extensión que poder encontrar la imagen*
	 * 
	 * @param filename
	 * @return {@link ResponseEntity} vamos a retornar un recurso o imagen a la
	 *         respuesta http
	 */
	@GetMapping("/uploads/{filename:.+}")
	public ResponseEntity<Resource> showPhoto(@PathVariable String filename) {
		Resource resource = null;
		try {
			resource = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	/** Ver el detalle y la foto del cliente */
	@GetMapping("/show/{id}")
	public String showClientDetail(@PathVariable(name = "id") Long id, Map<String, Object> model,
			RedirectAttributes flash) {
		Client client = clientService.fetchByIdWithInvoices(id);
		if (client == null) {
			flash.addFlashAttribute("error", "Client id doesn't exist in database");
			return "redirect:/list";
		}
		model.put("client", client);
		model.put("title", "Client Detail");
		return "clients/showdetails";
	}

	/**
	 * Despues de modificar el repositorio por PagingAndSortingRepository para
	 * paginar los resultados consulta e implementar el nuevo método que hace uso
	 * del iterable {@code Page} lo primero que tenemos que hacer es obtener la
	 * pagina actual mediante la ruta URL, por ejemplo podemos usar un
	 * {@code RequestParam}
	 */
	@GetMapping({ "/list", "/" })
	public String showClientsList(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication) {
		// Es importante validar la autenticacion
		if (authentication != null) {
			logger.info("Hi! ".concat(authentication.getName()));
		}
		// obtener la autenticacion de forma estática
		// Authentication auth= SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			logger.info("Hi!, with static mode ".concat(SecurityContextHolder.getContext().getAuthentication().getName()));
		}
		/*
		 * forma que se usaba en Springboot 1.5.* Pageable pageRequest = new
		 * PageRequest(page,size);
		 */
		Pageable pageRequest = PageRequest.of(page, 8);

		Page<Client> clients = clientService.findAll(pageRequest);

		PageRender<Client> pageRender = new PageRender<>("/list", clients);
		// pasando datos a la vista
		model.addAttribute("title", "Client List");
		model.addAttribute("clients", clients);
		model.addAttribute("page", pageRender);
		return "clients/clientlist";
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
		return "clients/form";
	}

	/**
	 * Como segunda fase cuando el usuario envía con un SUBMIT los datos del
	 * formulario, vamos a tener un metodo que se encarge de procesar estos datos
	 * aqui se elimina el atributo de session
	 */
	@PostMapping("/form")
	public String save(@Valid Client client, BindingResult result, Model model,
			@RequestParam("file") MultipartFile photoFile, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("title", "Client Form");
			/*
			 * cuando falla pasa la validación, Spring añade el objeto como atributo de
			 * forma automática, siempre y cuando el T tipo de la clase, se llame igual que
			 * el nombre con el cual se está pasando a la vista, en este caso client
			 */
			return "form";
		}
		if (!photoFile.isEmpty()) {
			if (client.getId() != null && client.getId() > 0 && client.getPhoto() != null
					&& client.getPhoto().length() > 0) {
				uploadFileService.delete(client.getPhoto());
			}
			String uniqueFileName = null;
			try {
				uniqueFileName = uploadFileService.copy(photoFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Has uploaded successufully '" + uniqueFileName + "'");

			client.setPhoto(uniqueFileName);

		}

		// tendremos que validar el id de cliente para saber si hace un insert o un
		// update para enviar el mensaje flash correspondiente
		String messageFlash = (client.getId() != null) ? "Client edited successfully" : "Client saved successfully";
		clientService.save(client);
		// va a eliminar el objeto cliente de la session
		status.setComplete();
		flash.addFlashAttribute("success", messageFlash);
		return "redirect:/list";

	}

	/**
	 * Para editar lo hacemos con el parametro id y en la vista list creamos el link
	 * para editar requestMapping sin el metodo por defeto es un GET
	 */
	@RequestMapping(value = "/form/{id}")
	public String edit(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Client client = null;
		if (id > 0) {
			client = clientService.findOne(id);
			if (client == null) {
				flash.addFlashAttribute("error", "Client id doesn't exist in database");
				return "redirect:/list";
			}
		} else {
			flash.addFlashAttribute("error", "Client id cannot be 0");
			return "redirect:/list";
		}
		model.put("client", client);
		model.put("title", "Edit CLient");
		return "clients/form";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Client client = clientService.findOne(id);
			clientService.delete(id);
			flash.addFlashAttribute("success", "client removed successfully");

			if (uploadFileService.delete(client.getPhoto())) {
				flash.addFlashAttribute("info", "Photo " + client.getPhoto() + " removed successfully!");
			}

		}
		return "redirect:/list";
	}
}
