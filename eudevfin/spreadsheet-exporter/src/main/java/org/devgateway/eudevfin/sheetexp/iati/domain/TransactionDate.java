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
package org.devgateway.eudevfin.sheetexp.iati.domain;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author alexandru-m-g
 *
 */
public class TransactionDate {
	@XStreamAlias("iso-date")
	@XStreamAsAttribute
	private Date isoDate;
	
	public TransactionDate(final Date isoDate) {
		super();
		this.isoDate = isoDate;
	}

	public Date getIsoDate() {
		return this.isoDate;
	}

	public void setIsoDate(final Date isoDate) {
		this.isoDate = isoDate;
	}
	
	
}
