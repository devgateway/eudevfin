/**
 * 
 */
package org.devgateway.eudevfin.auth.common.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.userdetails.User;

/**
 * @author mihai We named this {@link PersistedUser} not to confuse it with
 *         {@link User}
 */
@Entity
public class PersistedUser implements Serializable {

	private static final long serialVersionUID = 3330162033003739027L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true)
	private String username;

	@Column(length = 256)
	private String password;

	private boolean enabled = true;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<PersistedAuthority> authorites = new HashSet<PersistedAuthority>();

	@ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
	private Set<PersistedUserGroup> groups = new HashSet<PersistedUserGroup>();

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

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
	 * @return the authorites
	 */
	public Set<PersistedAuthority> getAuthorites() {
		return authorites;
	}

	/**
	 * @param authorites
	 *            the authorites to set
	 */
	public void setAuthorites(Set<PersistedAuthority> authorites) {
		this.authorites = authorites;
	}

	/**
	 * @return the groups
	 */
	public Set<PersistedUserGroup> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(Set<PersistedUserGroup> groups) {
		this.groups = groups;
	}

}
