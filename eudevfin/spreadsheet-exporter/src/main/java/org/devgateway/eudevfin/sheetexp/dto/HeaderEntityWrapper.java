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
package org.devgateway.eudevfin.sheetexp.dto;

import org.joda.time.LocalDateTime;

/**
 * @author Alex
 *
 */
public class HeaderEntityWrapper<T> implements EntityWrapperInterface<T> {
	
	private final String reportType;
	private final LocalDateTime processStartTime;
	private final String locale;
	

	public HeaderEntityWrapper(final String reportType, final LocalDateTime processStartTime, final String locale) {
		this.reportType = reportType;
		this.processStartTime = processStartTime;
		this.locale		= locale;
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface#isHeader()
	 */
	@Override
	public boolean isHeader() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface#reportType()
	 */
	@Override
	public String getReportType() {
		return this.reportType;
	}

	/* (non-Javadoc)
	 * @see org.devgateway.eudevfin.sheetexp.dto.EntityWrapperInterface#getEntity()
	 */
	@Override
	public T getEntity() {
		return null;
	}

	@Override
	public LocalDateTime getProcessStartTime() {
		return this.processStartTime;
	}

	@Override
	public String getLocale() {
		return this.locale;
	}

}
