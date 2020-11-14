package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.entity.Client;

/**
 * <ol>
 * <li>El siguiente paso es decorar la siguiente clase con la Annotation
 * {@code @Repository}, es una anotación de Spring para marcar la clase como
 * componente de persistencia de acceso a datos, a parte de ser un BEANS de
 * acceso a datos, se encarga de traducir correctamente y con detalle la
 * Exceptions que puedan ocurrir por ejemplo el {@code DataAccessException}</li>
 * <li>Luego tenemos que agregar el {@code EntityManager}: que se encarga de
 * manejar las clases de entidades, el ciclo de vida, las persiste dentro del
 * contexto, las actualiza, las elimina, puede realizar consultas, todas las
 * operaciones de una base de datos, pero a nivel de objeto, a travez de las
 * clases {@code Entity}</li>
 * </ol>
 * 
 */
@Repository("clientDaoJPA")
public class ClientDaoImpl implements IClientDao {
	// si no configuramos ningun tipo de proveedor JPA en el application.properties,
	// Spring boot va a utilizar la base de datos embedida en nuestra aplicacion
	@PersistenceContext
	private EntityManager em;

	/**
	 * con esta anotacion marcamos el método como transaccional y lo colocamos
	 * solamente de lectura {@code Transactional} es una anotacion que va a tomar el
	 * contenido del siguiento método t lo va a envolver dentro de una transaccion
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Client> findAll() {
		// vamos a realizar una consulta con .createQuery("from
		// <<Class>>").getResultList();
		return em.createQuery("from Client").getResultList();
	}

	/**
	 * Deberá ir anotado con el decorador {@code @Transactional}, ya que es de
	 * escritura, el método persist del entity manager, lo que hace es tomar el
	 * objeto Entity y lo guarda dentro del contexto de persistencia o persistencia
	 * JPA, una vez que se realize el commit() y el flush(), se va a sincronizar con
	 * la base de datos y va a realizar el INSERT INTO en la tabla, todo eso lo hace
	 * de forma automática y transparente dentro del
	 * {@code EntityManager.persist()},
	 */
	@Transactional
	@Override
	public void save(Client client) {
		em.persist(client);

	}

}
