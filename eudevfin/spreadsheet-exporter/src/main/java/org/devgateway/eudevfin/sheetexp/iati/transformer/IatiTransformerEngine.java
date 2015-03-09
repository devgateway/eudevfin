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
import java.util.List;

import org.apache.log4j.Logger;
import org.devgateway.eudevfin.financial.CustomFinancialTransaction;
import org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivities;
import org.devgateway.eudevfin.sheetexp.iati.domain.IatiActivity;
import org.devgateway.eudevfin.sheetexp.iati.transformer.util.Conditions;

/**
 * @author alexandru-m-g
 *
 */
public class IatiTransformerEngine {
	
	private static Logger logger = Logger.getLogger(IatiTransformerEngine.class);

	private final List<EntityWrapperInterface<CustomFinancialTransaction>> list;
	private final IatiActivities iatiActivities;
	
	private String lastCrsId = "";
	
	public IatiTransformerEngine(
			final List<EntityWrapperInterface<CustomFinancialTransaction>> list) {
		super();
		this.list = list;
		this.iatiActivities = new IatiActivities();
		this.iatiActivities.setActivities(new ArrayList<IatiActivity>());
	} 



	public IatiTransformerEngine transform() {
		IatiActivity currentActivity = null;
		int latestReportingYear = 0;
		for ( final EntityWrapperInterface<CustomFinancialTransaction> customTxWrapper: this.list ) {
			final CustomFinancialTransaction ctx	= customTxWrapper.getEntity();
			logger.debug( String.format("Processing transaction with id %s and title %s", ctx.getId(), ctx.getShortDescription()) ); 
			
			if ( !Conditions.SHOULD_SKIP(ctx)) {
				if ( ctx.getCrsIdentificationNumber() != null ) {
					IatiActivityEngine activityEngine;
					if ( this.isNewActivity(ctx) ) {
						activityEngine  = new IatiActivityEngine(ctx); 
						currentActivity = activityEngine.process().getIatiActivity();
						this.iatiActivities.getActivities().add(currentActivity);
					}
					else {
						activityEngine  = new IatiActivityEngine(ctx, currentActivity, latestReportingYear);
						currentActivity = activityEngine.process().getIatiActivity();
					}
					if (ctx.getReportingYear() != null) {
						latestReportingYear = ctx.getReportingYear().getYear();
					}
				}
				else {
					logger.error(String.format("Transaction with id %s and title %s has no CRS id.", ctx.getId(), ctx.getShortDescription()));
				}
			}
		}
		return this;
	}
	
	private boolean isNewActivity(final CustomFinancialTransaction ctx) {
		if ( this.lastCrsId.equals(ctx.getCrsIdentificationNumber()) ) {
			return false;
		}
		else {
			this.lastCrsId = ctx.getCrsIdentificationNumber();
			return true;
		}
	}

	public IatiActivities getIatiActivities() {
		return this.iatiActivities;
	}
	
	

}
