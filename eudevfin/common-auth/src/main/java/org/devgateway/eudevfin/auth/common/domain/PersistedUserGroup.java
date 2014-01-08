/**
 * 
 */
package org.devgateway.eudevfin.auth.common.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * @author mihai
 * 
 */
@Entity
public class PersistedUserGroup implements Serializable {

	private static final long serialVersionUID = -8690393283997583919L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true)
	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<PersistedAuthority> authorities = new HashSet<PersistedAuthority>();

	@ManyToMany
	private Set<PersistedUser> users = new HashSet<PersistedUser>();

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the authorities
	 */
	public Set<PersistedAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * @param authorities
	 *            the authorities to set
	 */
	public void setAuthorities(Set<PersistedAuthority> authorities) {
		this.authorities = authorities;
	}

	/**
	 * @return the users
	 */
	public Set<PersistedUser> getUsers() {
		return users;
	}

	/**
	 * @param users
	 *            the users to set
	 */
	public void setUsers(Set<PersistedUser> users) {
		this.users = users;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
