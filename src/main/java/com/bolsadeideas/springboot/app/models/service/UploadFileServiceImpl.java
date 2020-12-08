package com.bolsadeideas.springboot.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final static String UPLOADS_FOLDER = "uploads";

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path photoPath = getPath(filename);
		log.info("photoPath: " + photoPath);
		Resource resource = new UrlResource(photoPath.toUri());
		if (!resource.exists() || !resource.isReadable()) {
			log.error("Error: Resource cannot be loaded: " + photoPath.toString());
			throw new RuntimeException("Error: Resource cannot be loaded: " + photoPath.toString());
		}
		return resource;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		// Para evitar tener archivos con el mismo nombre y que no se reemplazen
		String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		// para que el directorio se encuentre en la raiz del proyecto y se va a llamar
		// uploads
		Path rootPath = getPath(uniqueFileName);

		log.info("rootPath: " + rootPath);

		// en vez de usar bytes podemos usar el método copy
		Files.copy(file.getInputStream(), rootPath);

		return uniqueFileName;
	}

	@Override
	public boolean delete(String filename) {
		Path rootPath = getPath(filename);
		File file = rootPath.toFile();
		if (file.exists() && file.canRead()) {
			if (file.delete()) {
				return true;
			}

		}
		return false;
	}

	public Path getPath(String filename) {
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());

	}

	@Override
	public void init() throws IOException {
		Files.createDirectory(Paths.get(UPLOADS_FOLDER));

	}

}
