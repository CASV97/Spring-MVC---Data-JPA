/*
 * package com.bolsadeideas.springboot.app;
 * 
 * import org.springframework.context.annotation.Configuration; import
 * org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 * 
 * @Configuration public class MvcConfig implements WebMvcConfigurer {
 * 
 * private final Logger log = LoggerFactory.getLogger(getClass());
 * 
 * 
 * @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
 * WebMvcConfigurer.super.addResourceHandlers(registry);
 * 
 * String resourcePath =
 * Paths.get("uploads").toAbsolutePath().toUri().toString();
 * log.info(resourcePath); // 1 Usamos la misma ruta a la que accede la vista
 * registry.addResourceHandler("/uploads/**") // 2 la ubicación, como es una
 * ruta un directorio físico usamos file que lo // agrega automaticamente
 * .addResourceLocations(resourcePath); }
 * 
 * 
 * }
 */