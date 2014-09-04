/*******************************************************************************
 * Copyright (c) 2014 Development Gateway.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *******************************************************************************/

package org.devgateway.eudevfin.ui.common.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author mihai
 */
public class ReportingYearChangedEventPayload extends AbstractAjaxUpdateEventPayload {
	private Integer reportingYear;
	
	public Integer getReportingYear() {
		return reportingYear;
	}

	public void setReportingYear(Integer reportingYear) {
		this.reportingYear = reportingYear;
	}

	public ReportingYearChangedEventPayload(AjaxRequestTarget target,Integer reportingYear) {
		super(target);
		this.reportingYear=reportingYear;
	}

}
