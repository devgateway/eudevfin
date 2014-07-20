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

import java.math.BigDecimal;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * @author alexandru-m-g
 *
 */
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"})
public class AmountValue {
	@XStreamAsAttribute
	@XStreamAlias("value-date")
	private Date valueDate;
	
	@XStreamAsAttribute
	private String currency;
	
	private BigDecimal value;
	
	public AmountValue() {}
	
	public AmountValue(final Date valueDate, final String currency, final BigDecimal value) {
		super();
		this.valueDate = valueDate;
		this.currency = currency;
		this.value = value;
	}


	public Date getValueDate() {
		return this.valueDate;
	}

	public void setValueDate(final Date valueDate) {
		this.valueDate = valueDate;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(final String currency) {
		this.currency = currency;
	}

	public BigDecimal getValue() {
		return this.value;
	}

	public void setValue(final BigDecimal value) {
		this.value = value;
	}


	
	
}
