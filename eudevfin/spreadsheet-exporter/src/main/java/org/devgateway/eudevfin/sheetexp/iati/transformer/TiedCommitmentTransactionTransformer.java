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
package org.devgateway.eudevfin.sheetexp.iati.transformer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.sheetexp.iati.domain.CodeEntity;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivity;
import org.devgateway.eudevfin.sheetexp.iati.domain.Transaction;
import org.devgateway.eudevfin.sheetexp.iati.transformer.util.Conditions;
import org.joda.money.BigMoney;

/**
 * @author alexandru-m-g
 *
 */
public class TiedCommitmentTransactionTransformer extends
		AbstractTransactionTransformer {

	
	/**
	 * @param ctx
	 * @param iatiActivity
	 * @param paramsMap
	 */
	public TiedCommitmentTransactionTransformer(final CustomFinancialTransaction ctx,
			final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
		super(ctx, iatiActivity, paramsMap);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.sheetexp.iati.transformer.AbstractTransactionTransformer#findTxDate()
	 */
	@Override
	protected Date findTxDate() {
		return this.getCtx().getCommitmentDate().toDate();
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.sheetexp.iati.transformer.AbstractTransactionTransformer#findTxMoney()
	 */
	@Override
	protected BigMoney findTxMoney() {
		return this.getCtx().getAmountsTied();
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.sheetexp.iati.transformer.AbstractTransactionTransformer#saveTransaction(org.devgateway.eudevfin.sheetexp.iati.domain.Transaction)
	 */
	@Override
	protected void saveTransaction(final Transaction transaction) {
		List<Transaction> transactions = this.getIatiActivity().getTransactions();
		if ( transactions == null ) {
			transactions = new ArrayList<Transaction>();
			this.getIatiActivity().setTransactions(transactions);
		}
		else if ( Conditions.SHOULD_OVERWRITE_TRANSACTION(this.getCtx())
				|| Conditions.IS_FSS(this.getCtx()) ) {
			final Iterator<Transaction> it = transactions.iterator();
			while (it.hasNext()) {
				final Transaction tempTx = it.next();
				if ( tempTx.getTransactionType() != null && "C".equals(tempTx.getTransactionType().getCode()) 
						&& tempTx.getTiedStatus() != null && this.getTiedStatusCode().equals(tempTx.getTiedStatus().getCode()) ){
					it.remove();
				} 
			}
		}
		
		transactions.add(transaction);
	}
	
	
	
	@Override
	protected Transaction createTransaction(final BigMoney money) {
		final Transaction transaction =  super.createTransaction(money);
		transaction.setTiedStatus(new CodeEntity(this.getTiedStatusCode(), null));
		transaction.setTransactionType(new CodeEntity("C", null));
		return transaction;
	}

	protected String getTiedStatusCode () {
		return "4";
	}

}
