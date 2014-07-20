/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
/**
 * 
 */
package org.devgateway.eudevfin.auth.common.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author mihai
 * 
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PersistedAuthority implements Serializable, Comparable<PersistedAuthority> {

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

	@Override
	public int compareTo(PersistedAuthority o) {
		return this.getAuthority().compareTo(o.getAuthority());
	}

}
