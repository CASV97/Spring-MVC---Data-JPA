package com.bolsadeideas.springboot.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.models.service.IUploadFileService;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {

	@Autowired
	private IUploadFileService uploadFileService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	private final Logger log = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	/**
	 * Aquí es donde vamos a crear el directorio uploads cada vez que se ejecute la
	 * aplicación
	 */
	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();

		String password2 = "ariel";
		String password3 = "admin";

		log.warn("\n ariel: " + passwordEncoder.encode(password2));
		log.warn("\n admin: " + passwordEncoder.encode(password3));

		log.warn("\nProyect Initialized");
	}

}
