/**
 *
 */
package org.devgateway.eudevfin.sheetexp.iati.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author alexandru-m-g
 *
 */
public class AbstractWithLanguage {
	@XStreamAlias("xml:lang")
	@XStreamAsAttribute
	protected String lang;

	public AbstractWithLanguage() {
		this.lang = "en";
	}

	public AbstractWithLanguage(final String lang) {
		super();
		this.lang = lang;
	}

	public String getLang() {
		return this.lang;
	}

	public void setLang(final String lang) {
		this.lang = lang;
	}


}
