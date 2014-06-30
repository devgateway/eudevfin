/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.iati.transformer;

import java.util.Date;
import java.util.Map;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivity;
import org.joda.time.LocalDateTime;

/**
 * @author alexandru-m-g
 *
 */
public abstract class AbstractElementTransformer {
	
	public static final String LATEST_YEAR = "latest-year";
	
	private final CustomFinancialTransaction ctx; 
	private final IatiActivity iatiActivity;
	
	private final Map<String, Object> paramsMap;

	
	public AbstractElementTransformer(final CustomFinancialTransaction ctx, final IatiActivity iatiActivity, 
			final Map<String, Object> paramsMap) {
		
		this.ctx = ctx;
		this.iatiActivity = iatiActivity;
		
		this.paramsMap = paramsMap;
		
	}
	
	public abstract void process();

	public CustomFinancialTransaction getCtx() {
		return this.ctx;
	}

	public IatiActivity getIatiActivity() {
		return this.iatiActivity;
	}

	public int getLatestYear() {
		final Integer latestYear = (Integer) this.paramsMap.get(LATEST_YEAR);
		return latestYear!=null ? latestYear:0;
	}
	
	protected Date findLastDayOfYear(final int year) {
		return new LocalDateTime(year, 12, 31, 7, 7).toDate();
	}
	
	protected Date findFirstDayOfYear(final int year) {
		return new LocalDateTime(year, 1, 1, 7, 7).toDate();
	}
	
	protected Date findCreationDate() {
		return this.getCtx().getCreatedDate();
	}
	
	
}
