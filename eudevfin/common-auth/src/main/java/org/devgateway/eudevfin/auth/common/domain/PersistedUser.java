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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.security.core.userdetails.User;

/**
 * @author mihai We named this {@link PersistedUser} not to confuse it with
 *         {@link User}
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PersistedUser implements Serializable {

	private static final long serialVersionUID = 3330162033003739027L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String phone;

	@Column(unique = true)
	private String username;

	@Column(length = 256)
	private String password;

	private boolean enabled = true;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<PersistedAuthority> authorities = new HashSet<PersistedAuthority>();

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
	public Set<PersistedAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * @param authorites
	 *            the authorites to set
	 */
	public void setAuthorities(Set<PersistedAuthority> authorites) {
		this.authorities = authorites;
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

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	
}
