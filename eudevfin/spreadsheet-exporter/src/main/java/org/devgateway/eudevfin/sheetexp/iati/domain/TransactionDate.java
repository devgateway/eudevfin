/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.iati.domain;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author alexandru-m-g
 *
 */
public class TransactionDate {
	@XStreamAlias("iso-date")
	@XStreamAsAttribute
	private Date isoDate;
	
	public TransactionDate(final Date isoDate) {
		super();
		this.isoDate = isoDate;
	}

	public Date getIsoDate() {
		return this.isoDate;
	}

	public void setIsoDate(final Date isoDate) {
		this.isoDate = isoDate;
	}
	
	
}
