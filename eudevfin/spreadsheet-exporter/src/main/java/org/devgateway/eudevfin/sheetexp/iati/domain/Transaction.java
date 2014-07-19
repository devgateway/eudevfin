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
@XStreamAlias("transaction")
public class Transaction {
	
	@XStreamAlias("transaction-type")
	private CodeEntity transactionType;
	
	private String description;
	
	@XStreamAlias("transaction-date")
	private TransactionDate transactionDate;
	
	@XStreamAlias("flow-type")
	private CodeEntity flowType;
	
	@XStreamAlias("finance-type")
	private CodeEntity financeType;
	
	@XStreamAlias("aid-type")
	private CodeEntity aidType;
	
	@XStreamAlias("tied-status")
	private CodeEntity tiedStatus;
	
	@XStreamAlias("value")
	private AmountValue amountValue;

	public CodeEntity getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(final CodeEntity transactionType) {
		this.transactionType = transactionType;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public TransactionDate getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(final TransactionDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public CodeEntity getFlowType() {
		return this.flowType;
	}

	public void setFlowType(final CodeEntity flowType) {
		this.flowType = flowType;
	}

	public CodeEntity getFinanceType() {
		return this.financeType;
	}

	public void setFinanceType(final CodeEntity financeType) {
		this.financeType = financeType;
	}

	public CodeEntity getAidType() {
		return this.aidType;
	}

	public void setAidType(final CodeEntity aidType) {
		this.aidType = aidType;
	}

	public CodeEntity getTiedStatus() {
		return this.tiedStatus;
	}

	public void setTiedStatus(final CodeEntity tiedStatus) {
		this.tiedStatus = tiedStatus;
	}

	public AmountValue getAmountValue() {
		return this.amountValue;
	}

	public void setAmountValue(final AmountValue amountValue) {
		this.amountValue = amountValue;
	}
	
	
	
	
}
