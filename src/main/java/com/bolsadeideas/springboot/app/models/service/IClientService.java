package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.entity.Invoice;
import com.bolsadeideas.springboot.app.models.entity.Product;

/***/
public interface IClientService {
	public List<Client> findAll();

	// tiene que ser especialmente del org.springframework.data.domain.Pageable;
	public Page<Client> findAll(Pageable pageable);

	public void save(Client client);

	public Client findOne(Long id);

	public void delete(Long id);

	public List<Product> findByName(String term);

	public void saveIvoice(Invoice invoice);

	public Product findProductById(Long id);
}
