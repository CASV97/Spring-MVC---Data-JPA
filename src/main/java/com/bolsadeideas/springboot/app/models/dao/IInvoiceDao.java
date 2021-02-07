package com.bolsadeideas.springboot.app.models.dao;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Invoice;

public interface IInvoiceDao extends CrudRepository<Invoice, Serializable> {
	
}
