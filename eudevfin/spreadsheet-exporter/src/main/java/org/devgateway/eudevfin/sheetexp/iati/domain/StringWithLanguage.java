package org.devgateway.eudevfin.sheetexp.iati.domain;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"})
public class StringWithLanguage extends AbstractWithLanguage {

	protected String value;


	public StringWithLanguage(){}

	public StringWithLanguage(final String value, final String language) {
		super(language);
		this.value = value;
	}



	public StringWithLanguage(final String value) {
		super();
		this.value = value;
	}



	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

}
