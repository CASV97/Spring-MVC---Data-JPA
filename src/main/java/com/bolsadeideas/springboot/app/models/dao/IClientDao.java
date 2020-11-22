package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Client;

/**
 * @author ariel
 *
 *         Convertiremos esta interfaz en nuestro CRUD Repository, para eso debe
 *         de extender de las interfaces <code> CrudRepository </code>o
 *         <code>JpaRepository </code> lo podemos inyectar sin necesidad de
 *         declarar el Bean o agragar una anotacion como {@code @Repository} por
 *         que por debajo ya es un componente Spring y hereda de CrudRepository
 */
public interface IClientDao extends CrudRepository<Client, Long> {

}
