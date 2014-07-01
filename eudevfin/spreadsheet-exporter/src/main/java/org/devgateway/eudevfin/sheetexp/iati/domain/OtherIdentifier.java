/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.iati.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * @author alexandru-m-g
 *
 */
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"})
public class OtherIdentifier {
	
	private String value;
	
	@XStreamAlias("owner-ref")
	@XStreamAsAttribute
	private String ownerRef;

	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getOwnerRef() {
		return this.ownerRef;
	}

	public void setOwnerRef(final String ownerRef) {
		this.ownerRef = ownerRef;
	}

	
	
}
