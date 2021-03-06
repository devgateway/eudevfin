/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/
package org.devgateway.eudevfin.common.spring.integration;

import java.io.Serializable;

/**
 * Wrapper for spring integration null response messages
 * @author mihai
 *
 * @param <ENTITY> the wrapped entity
 */
public final class NullableWrapper<ENTITY> implements Serializable {
	
	private static final long serialVersionUID = 6062123246121228806L;
	private ENTITY entity;
	
	public NullableWrapper(ENTITY entity) {
		this.entity=entity;
	}

	/**
	 * @return the bean
	 */
	public ENTITY getEntity() {
		return entity;
	}
	
	public boolean isNull() {
		return entity==null;
	}
	
}
