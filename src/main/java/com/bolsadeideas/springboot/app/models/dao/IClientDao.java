package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Client;

/**
 * <h2>Repository</h2> <br>
 * Convertiremos esta interfaz en nuestro CRUD Repository, para eso debe de
 * extender de las interfaces <code> CrudRepository </code>o
 * <code>JpaRepository </code> lo podemos inyectar sin necesidad de declarar el
 * Bean o agragar una anotacion como {@code @Repository} por que por debajo ya
 * es un componente Spring y hereda de CrudRepository, además Spring data jpa
 * nos facilita la paginación para una interfaz mas amigable y eficiente con los
 * recursos solo tiene que heredar de {@code PagingAndSortingRepository} que a
 * su vez hereda de {@code CrudRepository}
 * 
 * @author Ariel
 * @see PagingAndSortingRepository
 * @see CrudRepository
 * @see Repository
 */
public interface IClientDao extends PagingAndSortingRepository<Client, Long> {
	@Query("select c from Client c LEFT JOIN FETCH c.invoices i where c.id=?1")
	public Client fetchByIdWithInvoices(Long id);
}
