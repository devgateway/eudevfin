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

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author alexandru-m-g
 *
 */
@XStreamAlias("planned-disbursement")
public class PlannedDisbursement {
	@XStreamAlias("period-start")
	private TransactionDate periodStart;
	
	@XStreamAlias("period-end")
	private TransactionDate periodEnd;
	
	@XStreamAlias("value")
	private AmountValue amountValue;

	public TransactionDate getPeriodStart() {
		return this.periodStart;
	}

	public void setPeriodStart(final TransactionDate periodStart) {
		this.periodStart = periodStart;
	}

	public TransactionDate getPeriodEnd() {
		return this.periodEnd;
	}

	public void setPeriodEnd(final TransactionDate periodEnd) {
		this.periodEnd = periodEnd;
	}

	public AmountValue getAmountValue() {
		return this.amountValue;
	}

	public void setAmountValue(final AmountValue amountValue) {
		this.amountValue = amountValue;
	}
	
	
}
