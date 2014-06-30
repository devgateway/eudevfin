/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.iati.transformer.util;

import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.metadata.common.domain.Category;
import org.devgateway.eudevfin.metadata.common.util.CategoryConstants;
import org.devgateway.eudevfin.ui.common.temporary.SB;

/**
 * @author alexandru-m-g
 * 
 */
public class Conditions {
	public static boolean SHOULD_ADD_TRANSACTION(final CustomFinancialTransaction ctx) {
		final Category natureOfSubmission = ctx.getNatureOfSubmission();
		if ( natureOfSubmission != null && 
				CategoryConstants.NatureOfSubmission.PREVIOUSLY_REPORTED_ACTIVITY
				.equals(natureOfSubmission.getCode())) {
			return true;
		}
		return false;
	}
	
	public static boolean SHOULD_OVERWRITE_TRANSACTION(final CustomFinancialTransaction ctx) {
		final Category natureOfSubmission = ctx.getNatureOfSubmission();
		if ( natureOfSubmission != null ) {
			final String code = natureOfSubmission.getCode();
			if (CategoryConstants.NatureOfSubmission.NEW_ACTIVITY_REPORTED.equals(code) 
					|| CategoryConstants.NatureOfSubmission.REVISION.equals(code) 
					|| CategoryConstants.NatureOfSubmission.COMMITMENT_EQ_DISBURSEMENT.equals(code)) {
				return true;
			}
		}
		return false;
	}

	public static boolean SHOULD_SKIP(final CustomFinancialTransaction ctx) {
		final Category natureOfSubmission = ctx.getNatureOfSubmission();
		if (natureOfSubmission != null && 
				CategoryConstants.NatureOfSubmission.PROVISIONAL_DATA.equals(natureOfSubmission)) {
			return true;
		}
		return false;
	}
	
	public static boolean IS_REVISION(final CustomFinancialTransaction ctx) {
		final Category natureOfSubmission = ctx.getNatureOfSubmission();
		if ( natureOfSubmission != null && 
				CategoryConstants.NatureOfSubmission.REVISION
				.equals(natureOfSubmission.getCode())) {
			return true;
		}
		return false;
	}
	
	public static boolean IS_NEWER_OR_SAME_REP_YEAR(final CustomFinancialTransaction ctx, final Integer latestYear) {
		if ( ctx.getReportingYear() != null || ctx.getReportingYear().getYear() >= latestYear ) {
			return true;
		}
		return false;
		
	}

	public static boolean IS_FSS(final CustomFinancialTransaction ctx) {
		if ( SB.BILATERAL_ODA_FORWARD_SPENDING.equals(ctx.getFormType()) ) {
			return true;
		}
		return false;
	}
}
