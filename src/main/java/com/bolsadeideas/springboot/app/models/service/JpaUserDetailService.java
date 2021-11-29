/**
 * 
 */
package com.bolsadeideas.springboot.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IUserDao;
import com.bolsadeideas.springboot.app.models.entity.Role;
import com.bolsadeideas.springboot.app.models.entity.User;

/**
 * No necesitamos crear una interfaz ya que la interfaz la proporciona
 * SprinSecurity para trabajar con el proceso de Login
 * 
 * @author CASV97
 */
@Service("jpaUserDetailService")
public class JpaUserDetailService implements UserDetailsService {
	@Autowired
	private IUserDao userDao;
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * cagamos los datos del usuario y devolvemos un objeto del tipo UserDetail que
	 * representa el usuario identificadoS
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 1 obtenemos el usuario
		User user = userDao.findByUsername(username);
		if (user == null) {
			logger.info("User not exist: " + username);
			throw new UsernameNotFoundException("User not exist: " + username);
		}
		// 2 obtenemos los role y los registramos dentro de una lista de
		// GrantedAuthority
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		// por cada rol del usuario lo guardamos en la lista de roles
		for (Role role : user.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		if (authorities.isEmpty()) {
			logger.info("User " + username + " dont have assigned roles");
			throw new UsernameNotFoundException("User " + username + " dont have assigned roles");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.getEnable(), /* accountNonExpired */ true, /* credentialsNonExpired */ true,
				/* accountNonLocked */ true, authorities);
	}

}
