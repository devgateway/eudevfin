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
}
