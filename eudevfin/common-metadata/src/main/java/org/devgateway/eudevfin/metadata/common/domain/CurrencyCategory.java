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
package org.devgateway.eudevfin.metadata.common.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author Alex
 *
 */
@Entity
@Audited
@DiscriminatorValue("Currency")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CurrencyCategory extends Category {

	private static final long serialVersionUID = -7847940152929772336L;
	
	private boolean mainCurrency	= false;

	public boolean isMainCurrency() {
		return mainCurrency;
	}

	public void setMainCurrency(boolean mainCurrency) {
		this.mainCurrency = mainCurrency;
	}
	
	

}
