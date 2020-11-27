package com.bolsadeideas.springboot.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addResourceHandlers(registry);
		// 1 Usamos la misma ruta a la que accede la vista
		registry.addResourceHandler("/uploads/**")
				// 2 la ubicación, como es una ruta un directorio físico usamos file
				.addResourceLocations("file:D:/Captures/springUploads/");
	}

}
