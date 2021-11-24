package com.bolsadeideas.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
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
	@Secured("ROLE_USER")
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
	@Secured("ROLE_USER")
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
	@Secured("ROLE_USER")
	@GetMapping({ "/list", "/" })
	public String showClientsList(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request) {
		// Es importante validar la autenticacion
		if (authentication != null) {
			logger.info("Hi! ".concat(authentication.getName()));
		}
		/*
		 * obtener la autenticacion de forma estática Authentication auth=
		 * SecurityContextHolder.getContext().getAuthentication();
		 */
		if (authentication != null) {
			logger.info(
					"Hi!, with static mode ".concat(SecurityContextHolder.getContext().getAuthentication().getName()));
			/*
			 * Forma1 de validar rol se tiene mas control y se pueden acceder a cada uno de
			 * los roles y se puede trabajar con ellos
			 */
			if (hasRole("ROLE_ADMIN")) {
				logger.info("Hi! ".concat(authentication.getName()).concat(" you have access to admin functions"));
			} else {
				logger.info("Hi! ".concat(authentication.getName()).concat(" you have not access to admin functions"));
			}
			/*
			 * Forma2 Creamos la instancia de una clase de Spring Security
			 * (SecurityContextHolderAwareRequestWrapper) que envuelve el objeto httpRequest
			 * y nos permite validar el role, pero para esto necesitamos inyectar en el
			 * método del controlador el objeto HttpServletRequest, y permite validar por el
			 * prefijo del role, esto internamente hace lo mismo que hicimos al principio
			 * con el método hasRole ya que internamente tambien recorre una coleccion de
			 * roles y las valida
			 * 
			 */
			SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(
					request, "");
			if (securityContext.isUserInRole("ROLE_ADMIN")) {
				logger.info("Hi! ".concat(authentication.getName())
						.concat(" you have access to admin functions using SecurityContextHolderAwareRequestWrapper"));

			} else {

				logger.info("Hi! ".concat(authentication.getName()).concat(
						" you have not access to admin functions using SecurityContextHolderAwareRequestWrapper"));
			}
			/* Forma 3 con el ol objeto request */
			if (request.isUserInRole("ROLE_ADMIN")) {
				logger.info("Hi! ".concat(authentication.getName())
						.concat(" you have access to admin functions using HttpServletRequest"));

			} else {

				logger.info("Hi! ".concat(authentication.getName())
						.concat(" you have not access to admin functions using HttpServletRequest"));
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
		return "redirect:/login";
	}

	/**
	 * Se encarga de mostrar el formulario al usuario, y va a recibir el objeto
	 * Model, o puede recibir un Map de java.util, en vez de usar addAttribute,
	 * vamos a usar put
	 */
	@Secured("ROLE_ADMIN")
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
	@Secured("ROLE_ADMIN")
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
	@Secured("ROLE_ADMIN")
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

	@Secured("ROLE_ADMIN")
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

	/**
	 * Validar si el usuario tiene el rol de forma programatica, y obtenemos el
	 * objeto Authentication de forma estática
	 *
	 * 
	 * @return {@link Boolean} <code>false</code> si no tiene acceso
	 */
	private boolean hasRole(String roleName) {
		SecurityContext context = SecurityContextHolder.getContext();
		// si el contexto es nulo, no tiene acceso
		if (context == null) {
			return false;
		}
		Authentication auth = context.getAuthentication();
		// si la autenticacion es nula no tiene acceso
		if (auth == null) {
			return false;
		}
		/*
		 * Atravez del objeto Athentication obtenemos una coleccion de Roles o
		 * (Authorities) la coleccion es de cualquier tipo que implemente la interfaz
		 * GrantedAuthority, cualquier calse Role o que representa un rol en nuestra
		 * aplicacion, tiene que implemetar esta interfaz Con signo ? implementamos un
		 * generico, con esto decimos que es una coleccion de cualquier tipo de objeto
		 * que implemete o herede esta interfaz
		 */
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		/*
		 * iteramos y preguntamos por cada authority si es igual al que pasamos por
		 * parámetro,podemos obtener de esta forma el nombre del rol
		 */
		/*
		 * for (GrantedAuthority authority : authorities) { if
		 * (roleName.equals(authority.getAuthority())) {
		 * logger.info("Hi! ".concat(auth.getName()).concat(" your role is: ".concat(
		 * authority.getAuthority()))); return true; } } return false;
		 */
		/* En vez de usar el le pasamos una instancia concreta con el valor del role */
		return authorities.contains(new SimpleGrantedAuthority(roleName));

	}
}
