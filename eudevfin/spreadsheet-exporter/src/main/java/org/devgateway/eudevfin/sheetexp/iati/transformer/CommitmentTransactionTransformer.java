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
public class CommitmentTransactionTransformer extends
		AbstractTransactionTransformer {

	/**
	 * @param ctx
	 * @param iatiActivity
	 * @param paramsMap
	 */
	public CommitmentTransactionTransformer(final CustomFinancialTransaction ctx,
			final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
		super(ctx, iatiActivity, paramsMap);
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.sheetexp.iati.transformer.AbstractTransactionTransformer#findTxDate()
	 */
	@Override
	protected Date findTxDate() {
            if (this.getCtx().getCommitmentDate() != null) 
                return this.getCtx().getCommitmentDate().toDate();
            else
                return new Date();
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.sheetexp.iati.transformer.AbstractTransactionTransformer#findTxMoney()
	 */
	@Override
	protected BigMoney findTxMoney() {
		BigMoney commitments = this.getCtx().getCommitments();
		if ( commitments != null ) {
			final BigMoney amountsTied = this.getCtx().getAmountsTied();
			if ( amountsTied != null ) {
				commitments = commitments.minus(amountsTied);
			}
			
			final BigMoney amountsUntied = this.getCtx().getAmountsUntied();
			if ( amountsUntied != null ) {
				commitments = commitments.minus(amountsUntied);
			}
			
			final BigMoney amountsPartiallyUntied = this.getCtx().getAmountsPartiallyUntied();
			if ( amountsPartiallyUntied != null ) {
				commitments = commitments.minus(amountsPartiallyUntied);
			}
			
			final double doubleValue = commitments.getAmount().doubleValue();
			if ( doubleValue < 0.1 ) {
				return null;
			}
		}
		return commitments;
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
						&& tempTx.getTiedStatus() == null ){
					it.remove();
				} 
			}
		}
		
		transactions.add(transaction);
	}
	
	@Override
	protected Transaction createTransaction(final BigMoney money) {
		final Transaction transaction =  super.createTransaction(money);
		transaction.setTransactionType(new CodeEntity("C", null));
		return transaction;
	}

}
