/**
 * 
 */
package org.devgateway.eudevfin.auth.common.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author mihai
 *
 */
@Entity
@Table(name="authorities")
public class Authority {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional= false )
	@JoinColumn(name="username")
	private User username;
	
	private String authority;

	/**
	 * @return the username
	 */
	public User getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(User username) {
		this.username = username;
	}

	/**
	 * @return the authority
	 */
	public String getAuthority() {
		return authority;
	}

	/**
	 * @param authority the authority to set
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	
	
}
