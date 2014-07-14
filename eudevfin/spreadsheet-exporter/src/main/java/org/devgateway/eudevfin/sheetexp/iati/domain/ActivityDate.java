/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.iati.domain;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * @author alexandru-m-g
 *
 */
@XStreamAlias("activity-date")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"})
public class ActivityDate {
	
	@XStreamAsAttribute
	private String type;
	
	private Date value;
	
	public ActivityDate(final String type, final Date value) {
		super();
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public Date getValue() {
		return this.value;
	}

	public void setValue(final Date value) {
		this.value = value;
	}
	
	
	
}
