/**
 *
 */
package org.devgateway.eudevfin.sheetexp.iati.domain;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author alexandru-m-g
 *
 */
public class CodeVocabSignEntityWithLanguage extends AbstractWithLanguage {
	@XStreamAsAttribute
	private String vocabulary;

	@XStreamAsAttribute
	private String significance;

	@XStreamAsAttribute
	private String code;

	public String getVocabulary() {
		return this.vocabulary;
	}

	public void setVocabulary(final String vocabulary) {
		this.vocabulary = vocabulary;
	}

	public String getSignificance() {
		return this.significance;
	}

	public void setSignificance(final String significance) {
		this.significance = significance;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(final String code) {
		this.code = code;
	}
}
