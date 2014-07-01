/**
 * 
 */
package org.devgateway.eudevfin.sheetexp.iati.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * @author alexandru-m-g
 *
 */
@XStreamAlias("policy-marker")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value"})
public class PolicyMarker extends CodeVocabSignEntity {
	
	
	
	public PolicyMarker(final String value, final String code, final String vocab, final String significance) {
		super();
		this.value = value;
		this.setCode(code);
		this.setVocabulary(vocab);
		this.setSignificance(significance);
	}

	private String value;

	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}
	
}
