/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.iati.transformer;

import java.util.Map;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivity;
import org.joda.money.BigMoney;

/**
 * @author alexandru-m-g
 *
 */
public class PartiallyTiedCommitmentTransactionTransformer extends
		TiedCommitmentTransactionTransformer {

	/**
	 * @param ctx
	 * @param iatiActivity
	 * @param paramsMap
	 */
	public PartiallyTiedCommitmentTransactionTransformer(
			final CustomFinancialTransaction ctx, final IatiActivity iatiActivity,
			final Map<String, Object> paramsMap) {
		super(ctx, iatiActivity, paramsMap);
	}

	@Override
	protected BigMoney findTxMoney() {
		return this.getCtx().getAmountsPartiallyUntied();
	}

	@Override
	protected String getTiedStatusCode() {
		return "3";
	}

	
}
