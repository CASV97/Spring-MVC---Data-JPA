package com.bolsadeideas.springboot.app.models.dao;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Invoice;

public interface IInvoiceDao extends CrudRepository<Invoice, Serializable> {

	/**
	 * Optimizando las consultas de las facturas con todas las relaciones hechas,
	 * sin tener que realizar las consultas hechas despues en la vista de forma LAZY
	 * o perezosa sinó que vengan todas las relaciones de una sola vez Con el
	 * cliente, los items de la factura y tambien los productos hacemos un select a
	 * la clase Entity de la factura, luego realizamos los JOINS indicando el
	 * atributo cliente del objeto Invoice y le damos un Alias c
	 */
	@Query("select i from Invoice i JOIN FETCH i.client c JOIN FETCH i.items l JOIN FETCH l.product where i.id=?1")
	public Invoice fecthByIdWithClientWithInvoiceLineItemWithProduct(Long id);
}
