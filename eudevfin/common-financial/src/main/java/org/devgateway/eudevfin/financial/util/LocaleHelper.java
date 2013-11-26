/**
 * 
 */
package org.devgateway.eudevfin.financial.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Alex
 *
 */
@Component
@Scope("request")
public class LocaleHelper {
	private String locale;

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	
}
