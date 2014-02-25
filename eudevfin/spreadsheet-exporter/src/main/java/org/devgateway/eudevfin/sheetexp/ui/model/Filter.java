/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.ui.model;

import java.io.Serializable;

/**
 * @author Alex
 *
 */
public class Filter implements Serializable {

	private Integer year;
	
	private String exportType;

	public Integer getYear() {
		return this.year;
	}

	public void setYear(final Integer year) {
		this.year = year;
	}

	/**
	 * @return the exportType
	 */
	public String getExportType() {
		return this.exportType;
	}

	/**
	 * @param exportType the exportType to set
	 */
	public void setExportType(final String exportType) {
		this.exportType = exportType;
	}
	
	
}
