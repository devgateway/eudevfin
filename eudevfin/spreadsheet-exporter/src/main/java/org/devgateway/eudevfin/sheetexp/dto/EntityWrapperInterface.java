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
