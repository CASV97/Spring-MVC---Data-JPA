/**
 * 
 */
package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.User;

/**
 * @author CASV97
 *
 */
public interface IUserDao extends CrudRepository<User, Long> {
//Consulta a travez del nombre de metodo se ejecutara la consulta JPQL
	// select u from User u where u.username=?1
	public User findByUsername(String username);
}
