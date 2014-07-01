package org.devgateway.eudevfin.sheetexp.iati.domain;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * @author alexandru-m-g
 *
 */
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"})
public class CodeEntity {
	private String value;
	
	@XStreamAsAttribute
	private String code;
	
	public CodeEntity(){}
	
	public CodeEntity(final String code, final String value) {
		super();
		this.value = value;
		this.code = code;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(final String code) {
		this.code = code;
	}
	
	

}
