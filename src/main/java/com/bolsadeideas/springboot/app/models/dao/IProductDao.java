package com.bolsadeideas.springboot.app.models.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Product;

public interface IProductDao extends CrudRepository<Product, Serializable> {

	/**
	 * Método para apartado de autocomplete, este método debe retornar una lista con
	 * los resultados será una consulta del tipo Where like '%%' para encontrar por
	 * el nombre de los productos, existen dos alternativas, la primera usando
	 * anotaciones con la anotacion @Query,que nos permite realizar un SELECT a
	 * nivel de objetos o Entities
	 */
	@Query("select p from Product p where p.name like %?1%")
	public List<Product> findByName(String term);

}
