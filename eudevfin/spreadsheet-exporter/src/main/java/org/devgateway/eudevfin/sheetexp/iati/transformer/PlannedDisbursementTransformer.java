/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.iati.transformer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.sheetexp.iati.domain.AmountValue;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivity;
import org.devgateway.eudevfin.sheetexp.iati.domain.PlannedDisbursement;
import org.devgateway.eudevfin.sheetexp.iati.domain.TransactionDate;
import org.devgateway.eudevfin.sheetexp.iati.transformer.util.Conditions;
import org.joda.money.BigMoney;
import org.joda.time.LocalDateTime;

/**
 * @author alexandru-m-g
 *
 */
public class PlannedDisbursementTransformer extends AbstractElementTransformer {
	
	private static Logger logger = Logger.getLogger(PlannedDisbursementTransformer.class);
	
	public PlannedDisbursementTransformer(final CustomFinancialTransaction ctx,
			final IatiActivity iatiActivity, final Map<String, Object> paramsMap) {
		super(ctx, iatiActivity, paramsMap);
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.sheetexp.iati.transformer.AbstractElementTransformer#process()
	 */
	@Override
	public void process() {
		if ( Conditions.IS_FSS(this.getCtx()) ){
			final Integer currentYear = this.findCurrentYear();
			if ( currentYear != null ) {
				int index = 0;
				for ( final BigMoney plannedDisbMoney: this.findPlannedDisbursements() ){
					if ( plannedDisbMoney != null ) {
						final PlannedDisbursement pd = this.createPlannedDisbursement(currentYear+index, plannedDisbMoney);
						this.savePlannedDisbursement(pd);
					}
					index++;
				}
			} else {
				logger.error(String.format("Fss Transaction '%s' has no reporting year ", this.getCtx().getShortDescription() ));
			}
		}

	}

	private void savePlannedDisbursement(final PlannedDisbursement pd) {
		List<PlannedDisbursement> pdList = this.getIatiActivity().getPlannedDisbursements();
		if ( pdList == null ) {
			pdList = new ArrayList<PlannedDisbursement>();
			this.getIatiActivity().setPlannedDisbursements(pdList);
		}
		
		boolean added = false;
		final ListIterator<PlannedDisbursement> iter = pdList.listIterator();
		while(iter.hasNext()) {
			final PlannedDisbursement pdItem = iter.next();
			if ( pdItem.getPeriodStart().equals(pd.getPeriodStart()) && pdItem.getPeriodEnd().equals(pd.getPeriodEnd()) ) {
				iter.set(pd);
				added = true;
				break;
			}
		}
		
		if ( !added ) {
			pdList.add(pd);
		}
		
	}

	private PlannedDisbursement createPlannedDisbursement(final Integer year, final BigMoney plannedDisbMoney) {
		
		final String curr = plannedDisbMoney.getCurrencyUnit().getCurrencyCode();
		final BigDecimal amount = plannedDisbMoney.getAmount();
		
		final PlannedDisbursement pd = new PlannedDisbursement();
		pd.setPeriodStart(new TransactionDate(this.findFirstDayOfYear(year)));
		pd.setPeriodEnd(new TransactionDate(this.findLastDayOfYear(year)));
		
		pd.setAmountValue(new AmountValue(this.findCreationDate(), curr, amount));
		
		return pd;
	}
	
	private Integer findCurrentYear() {
		final LocalDateTime ldt = this.getCtx().getReportingYear();
		if ( ldt != null ) {
			return ldt.getYear()  + 1;
		}
		return null;
	}
	
	
	private List<BigMoney> findPlannedDisbursements() {
		final List<BigMoney> retList = new LinkedList<>();
		retList.add(this.getCtx().getBudgetMTEFDisbursement());
		retList.add(this.getCtx().getBudgetMTEFDisbursementP1());
		retList.add(this.getCtx().getBudgetMTEFDisbursementP2());
		retList.add(this.getCtx().getBudgetMTEFDisbursementP3());
		retList.add(this.getCtx().getBudgetMTEFDisbursementP4());
		
		return retList;
	}

}
