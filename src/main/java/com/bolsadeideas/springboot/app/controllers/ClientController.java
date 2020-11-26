package com.bolsadeideas.springboot.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.bolsadeideas.springboot.app.service.IClientService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("client")
public class ClientController {
	@Autowired
	private IClientService clientService;

	/**
	 * Despues de modificar el repositorio por PagingAndSortingRepository para
	 * paginar los resultados consulta e implementar el nuevo método que hace uso
	 * del iterable {@code Page} lo primero que tenemos que hacer es obtener la
	 * pagina actual mediante la ruta URL, por ejemplo podemos usar un
	 * {@code RequestParam}
	 */
	@GetMapping("/list")
	public String showClientsList(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
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
		/*
		 * verificando que el fichero no esté vacio para para poder concatenar el nombre
		 * del archivo para poder escribir o mover la imagen es ese directorio
		 */
		if (!photoFile.isEmpty()) {
			Path uploadsResourcesPath = Paths.get("src//main//resources//static/uploads");
			String rootPath = uploadsResourcesPath.toFile().getAbsolutePath();
			// obteniendo los bytes de la imagen
			try {
				byte[] bytes = photoFile.getBytes();
				// lo siguiente es tener la ruta final con el nombre del archivo
				Path completePath = Paths.get(rootPath + "//" + photoFile.getOriginalFilename());
				// finalmente con escribimos la imagen en el directorio upload
				Files.write(completePath, bytes);
				flash.addFlashAttribute("info", "Has uploaded successufully '" + photoFile.getOriginalFilename() + "'");

				client.setPhoto(photoFile.getOriginalFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// tendremos que validar el id de cliente para saber si hace un insert o un
		// update para enviar el mensaje flash correspondiente
		String messageFlash = (client.getId() != null) ? "Client edited successfully" : "Client saved successfully";
		clientService.save(client);
		// va a eliminar el objeto cliente de la session
		status.setComplete();
		flash.addFlashAttribute("success", messageFlash);
		return "redirect:list";

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
		return "form";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			clientService.delete(id);
			flash.addFlashAttribute("success", "client removed successfully");
		}
		return "redirect:/list";
	}
}
