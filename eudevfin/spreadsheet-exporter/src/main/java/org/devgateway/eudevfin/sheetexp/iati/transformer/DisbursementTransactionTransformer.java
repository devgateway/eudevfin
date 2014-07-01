package org.devgateway.eudevfin.sheetexp.iati.transformer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.sheetexp.iati.domain.CodeEntity;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivity;
import org.devgateway.eudevfin.sheetexp.iati.domain.Transaction;
import org.devgateway.eudevfin.sheetexp.iati.transformer.util.Conditions;
import org.joda.money.BigMoney;


public class DisbursementTransactionTransformer extends
		AbstractTransactionTransformer {
	
	private static Logger logger = Logger.getLogger(DisbursementTransactionTransformer.class);

	public DisbursementTransactionTransformer(final CustomFinancialTransaction ctx,
			final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
		super(ctx, iatiActivity, paramsMap);
	}
	
	

	@Override
	protected Transaction createTransaction(final BigMoney money) {
		
		final Transaction tx =  super.createTransaction(money);
		tx.setTransactionType(new CodeEntity("D", null));
		
		return tx;
		
	}



	@Override
	protected Date findTxDate() {
		if ( this.getCtx().getReportingYear() != null ) {
			final int year =  this.getCtx().getReportingYear().getYear();
			return this.findLastDayOfYear(year);
		}
		logger.error(String.format("Reporting year is null for transaction '%'", this.getCtx().getShortDescription()) );
		return null;
	}

	@Override
	protected BigMoney findTxMoney() {
		return this.getCtx().getAmountsExtended();
	}

	@Override
	protected void saveTransaction(final Transaction transaction) {
		List<Transaction> transactions = this.getIatiActivity().getTransactions();
		if ( transactions == null ) {
			transactions = new ArrayList<Transaction>();
			this.getIatiActivity().setTransactions(transactions);
		}
		else if ( Conditions.SHOULD_OVERWRITE_TRANSACTION(this.getCtx())) {
			final Iterator<Transaction> it = transactions.iterator();
			while (it.hasNext()) {
				final Transaction tempTx = it.next();
				if ( tempTx.getTransactionType() != null && "D".equals(tempTx.getTransactionType().getCode()) ){
					it.remove();
				} 
			}
		}
		
		transactions.add(transaction);

	}

}
