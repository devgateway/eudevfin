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
public interface EntityWrapperInterface<T> {
	
	public boolean isHeader();
	public String getReportType();
	
	public LocalDateTime getProcessStartTime();
	
	public T getEntity();
	
	public String getLocale();
	
}
