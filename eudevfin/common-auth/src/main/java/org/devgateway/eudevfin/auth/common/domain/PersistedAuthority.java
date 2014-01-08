/**
 * 
 */
package org.devgateway.eudevfin.auth.common.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author mihai
 * 
 */
@Entity
public class PersistedAuthority implements Serializable {

	private static final long serialVersionUID = 1277503635558596280L;

	private String authority;

	@Id
	@Column(name = "authority")
	public String getAuthority() {
		return authority;
	}

	public PersistedAuthority() {
		
	}
	
	public PersistedAuthority(String authority) {
		this.authority = authority;
	}

	/**
	 * @param authority the authority to set
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	@Override
	public String toString() {
		return authority;
	}

}
