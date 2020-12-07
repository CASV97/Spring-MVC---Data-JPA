package com.bolsadeideas.springboot.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {
	/**
	 * Para poder cargar la imagen, va a retornar un recurso de Spring
	 * 
	 * @return {@link Resource}
	 */
	public Resource load(String filename) throws MalformedURLException;

	/**
	 * va a tomar la imagen original, la copia al nuevo directorio y la renombra
	 * 
	 * @return {@link String} nombre cambiado de la imagen o nobre único
	 */
	public String copy(MultipartFile file) throws IOException;

	/**
	 * Delete retorna un boolean para saber si eliminó o no el archivo y de esa
	 * forma saber si tenemos que enviar un mensaje flash en el controlador
	 * 
	 * @return isDelete
	 */
	public boolean delete(String filename);

}
