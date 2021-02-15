package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IClientDao;
import com.bolsadeideas.springboot.app.models.dao.IInvoiceDao;
import com.bolsadeideas.springboot.app.models.dao.IProductDao;
import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.entity.Invoice;
import com.bolsadeideas.springboot.app.models.entity.Product;

/**
 * Una clase service esta basado en el patron de diseño FASCADE o fachada,
 * <b>(Un único punto de acceso hacia distintos DAO's o repositorios) </b>, que
 * estan dentro de una clase servicio como atributo y podríamos operar con con
 * diferentes clases Dao, además evitamos tener que acceder de forma directa a
 * los DAOs dentro de los Controllers <br>
 * Por cada método en la clase DAO vamos a tener métodos en la clase métodos en
 * clases services, la implementacion es bastante simple, como es una fachada,
 * accedemos a los métodos del Dao <br>
 * Otra Caracteristica de la clase Service es que podemos manejar la transaccion
 * sin tener que implementar las anotaciones {@code @transactional} en el Dao
 * 
 */
@Service
public class ClientServiceImpl implements IClientService {
	@Autowired
	private IClientDao clientDao;

	@Autowired
	private IProductDao productDao;

	@Autowired
	private IInvoiceDao invoiceDao;

	@Transactional(readOnly = true)
	@Override
	public List<Client> findAll() {
		return (List<Client>) clientDao.findAll();
	}

	@Transactional
	@Override
	public void save(Client client) {
		clientDao.save(client);

	}

	/**
	 * @return devulve un Optional<Client> que e nvielve el resultado de la consulta
	 *         o el objeto, permite controlar errores del objeto
	 */
	@Transactional(readOnly = true)
	@Override
	public Client findOne(Long id) {
		return clientDao.findById(id).orElse(null);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		clientDao.deleteById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Client> findAll(Pageable pageable) {
		return clientDao.findAll(pageable);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Product> findByName(String term) {
		return productDao.findByName(term);
	}

	@Override
	@Transactional
	public void saveIvoice(Invoice invoice) {

		invoiceDao.save(invoice);
	}

	@Override
	@Transactional(readOnly = true)
	public Product findProductById(Long id) {
		// Recordando que cuando usamos Spring boot 2 o superior, este método retorna un
		// Optional
		return productDao.findById(id).orElseGet(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Invoice findInvoiceById(Long id) {
		return invoiceDao.findById(id).orElse(null);
	}

}
