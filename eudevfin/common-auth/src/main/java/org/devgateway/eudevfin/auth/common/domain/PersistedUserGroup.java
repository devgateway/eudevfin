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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.devgateway.eudevfin.financial.Organization;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author mihai
 * 
 */
@Audited
@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class PersistedUserGroup implements Serializable {

	private static final long serialVersionUID = -8690393283997583919L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true)
	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<PersistedAuthority> persistedAuthorities = new HashSet<PersistedAuthority>();

	@OneToMany(fetch = FetchType.EAGER,mappedBy="group")	
	private Set<PersistedUser> users = new HashSet<PersistedUser>();
	
	@ManyToOne(fetch = FetchType.EAGER,optional=false)
	private Organization organization;

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
	public Set<PersistedAuthority> getPersistedAuthorities() {
		return persistedAuthorities;
	}

	/**
	 * @param authorities
	 *            the authorities to set
	 */
	public void setPersistedAuthorities(Set<PersistedAuthority> authorities) {
		this.persistedAuthorities = authorities;
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

	/**
	 * @return the organization
	 */
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
