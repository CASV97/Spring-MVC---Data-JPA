package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import com.bolsadeideas.springboot.app.models.entity.Client;

/**
 * @author ariel
 *
 *         Lo que primero creamos para el DATA ACCESS OBJECT o antes de crear la
 *         clase es la interfaz que, <b>contiene el protocolo de
 *         comportamiento</b>, es decir, los métodos que tienen que implementar
 *         la clase DAO
 */
public interface IClientDao {

	public List<Client> findAll();

	public void save(Client client);
}
