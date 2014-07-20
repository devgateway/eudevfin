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
public class DefaultEntityWrapper<T> implements EntityWrapperInterface<T> {

	private final String reportType;
	
	private final LocalDateTime processStartTime;
	
	private final T entity;

	private final String locale;
	

	public DefaultEntityWrapper(final String reportType, final LocalDateTime processStartTime, final String locale, final T entity) {
		super();
		this.reportType = reportType;
		this.processStartTime = processStartTime;
		this.entity = entity;
		this.locale	= locale;
	}

	@Override
	public boolean isHeader() {
		return false;
	}

	@Override
	public String getReportType() {
		return this.reportType;
	}

	@Override
	public T getEntity() {
		return this.entity;
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
